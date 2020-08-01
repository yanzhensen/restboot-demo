package com.sam.project.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.project.sys.model.entity.User;
import com.sam.project.sys.model.parm.UserPARM;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface IUserService extends IService<User> {

    /**
     * 获取登录人信息
     *
     * @return
     */
    User sessionUser();

    /**
     * 根据用户ID获取角色
     *
     * @param uid
     * @return
     */
    List<Integer> getRoleIds(Integer uid);

    /**
     * 添加用户角色
     *
     * @param uid
     * @param roleIds
     */
    void saveUserRoles(Integer uid, List<Integer> roleIds);

    /**
     * 创建角色用户
     *
     * @param userPARM
     */
    void createUser(UserPARM userPARM);

    /**
     * 用户登录
     *
     * @param loginName 用户名
     * @param password  密码
     * @param ipAddr    登录地址
     * @return
     */
    JSONObject login(String loginName, String password, String ipAddr);
}
