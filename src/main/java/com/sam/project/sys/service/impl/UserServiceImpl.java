package com.sam.project.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.common.utils.ApiUtils;
import com.sam.common.utils.JWTUtils;
import com.sam.enums.StatusEnum;
import com.sam.framework.config.Global;
import com.sam.framework.cons.KeyCons;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import com.sam.framework.utils.RedisUtils;
import com.sam.project.sys.mapper.UserMapper;
import com.sam.project.sys.model.entity.User;
import com.sam.project.sys.model.entity.UserRole;
import com.sam.project.sys.model.parm.UserPARM;
import com.sam.project.sys.service.IUserRoleService;
import com.sam.project.sys.service.IUserService;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private IUserRoleService userRoleService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public User sessionUser() {
        return this.getById(ApiUtils.currentUid());
    }

    @Override
    public List<Integer> getRoleIds(Integer uid) {
        return userRoleService.lambdaQuery().select(UserRole::getRoleId, UserRole::getUid)
                .eq(UserRole::getUid, uid).list().stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserPARM userPARM) {
        User user = userPARM.convert(User.class);
        user.setCreateUid(ApiUtils.currentUid());
        //加盐加密
        user.setPassword(Md5Crypt.apr1Crypt(user.getPassword(), Global.getPwdSalt()));
        //默认可用
        user.setStatus(StatusEnum.NORMAL);
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("添加用户失败"), save(user));
        saveUserRoles(user.getUid(), userPARM.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Integer uid, List<Integer> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("移除权限失败"),
                    userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUid, uid)));
            ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("新增权限失败"),
                    userRoleService.saveBatch(roleIds.stream().map(e -> new UserRole(uid, e)).collect(Collectors.toList())));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject login(String loginName, String password, String ipAddr) {
        User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getLoginName, loginName), false);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("用户不存在"), user);
        System.out.println("Md5Crypt.apr1Crypt(password, Global.getPwdSalt()) = " + Md5Crypt.apr1Crypt(password, Global.getPwdSalt()));
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("用户名密码错误"), Md5Crypt.apr1Crypt(password, Global.getPwdSalt()).equals(user.getPassword()));
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("用户被禁用"), StatusEnum.NORMAL == user.getStatus());
        user.setLoginIp(ipAddr);
        updateById(user);
        String token = JWTUtils.generate(user.getUid());
        //设置token30分钟过期
        redisUtils.setEx(KeyCons.USER_ACCESS_TOKEN + user.getUid(), token, 30, TimeUnit.MINUTES);
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("uid", user.getUid());
        obj.put("loginName", loginName);
        obj.put("username", user.getUsername());
        obj.put("systemType", "AAA管理系统");
        obj.put("describe", "本系统为测试系统，功能正在开发");
        return obj;
    }
}
