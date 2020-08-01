package com.sam.project.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.config.Global;
import com.sam.framework.controller.SuperController;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.enums.StatusEnum;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.ApiAssert;
import com.sam.project.sys.model.dto.UserDTO;
import com.sam.project.sys.model.entity.User;
import com.sam.project.sys.model.parm.UserPARM;
import com.sam.project.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"用户操作相关接口"})
@Validated
@RestController
@RequestMapping("/users")
public class UserRestController extends SuperController {

    @Autowired
    private IUserService userService;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("查询所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名 模糊", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "真实姓名 模糊", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "用户状态", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<UserDTO>> page(@RequestParam(value = "loginName", required = false) String loginName,
                                             @RequestParam(value = "username", required = false) String username,
                                             @RequestParam(value = "status", required = false) StatusEnum status) {
        IPage<User> userIPage = userService.page(this.getPage(), new LambdaQueryWrapper<User>()
                .select(User.class, info -> !StringUtils.equalsAny(info.getColumn(), "password", "login_ip"))//排除两个字段
                .like(StringUtils.isNotEmpty(loginName), User::getLoginName, loginName)
                .like(StringUtils.isNotEmpty(username), User::getUsername, username)
                .eq(Objects.nonNull(status), User::getStatus, status));
        return success(userIPage.convert(e -> e.convert(UserDTO.class)));
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("查询单个用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @GetMapping("/{id}")
    public ApiResponses<UserDTO> get(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("未找到该用户"), user);
        UserDTO userDTO = user.convert(UserDTO.class);
        List<Integer> roleIds = userService.getRoleIds(user.getUid());
        userDTO.setRoleIds(roleIds);
        return success(userDTO);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("重置用户密码")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @PutMapping("/{id}/password")
    public ApiResponses<Void> resetPwd(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("未找到改用户"), user);
        user.setPassword(Md5Crypt.apr1Crypt(user.getLoginName(), Global.getPwdSalt()));
        userService.updateById(user);
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("设置用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "status", value = "用户状态", paramType = "query")
    })
    @PutMapping("/{id}/status")
    public ApiResponses<Void> updateStatus(@PathVariable("id") Integer id, @RequestParam(value = "status") StatusEnum status) {
        User user = userService.getById(id);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("未找到该用户"), user);
        user.setStatus(status);
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("修改用户失败"), userService.updateById(user));
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("创建用户")
    @PostMapping
    public ApiResponses<Void> create(@RequestBody @Validated(UserPARM.Create.class) UserPARM userPARM) {
        int count = userService.count(new LambdaQueryWrapper<User>().eq(User::getLoginName, userPARM.getLoginName()));
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("用户已存在"), count == 0);
        userService.createUser(userPARM);
        return success(HttpStatus.CREATED);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("修改用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @PutMapping("/{id}")
    public ApiResponses<Void> update(@PathVariable("id") Integer id, @RequestBody @Validated(UserPARM.Update.class) UserPARM userPARM) {
        User user = userPARM.convert(User.class);
        user.setUid(id);
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("修改用户失败"), userService.updateById(user));
        userService.saveUserRoles(id, userPARM.getRoleIds());
        return success();
    }
}

