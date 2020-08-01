package com.sam.project.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.sam.common.annotations.Resources;
import com.sam.common.utils.ApiUtils;
import com.sam.common.utils.IpUtils;
import com.sam.framework.cons.KeyCons;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.LogUtils;
import com.sam.framework.utils.RedisUtils;
import com.sam.project.sys.model.parm.LoginPARM;
import com.sam.project.sys.model.parm.UserPARM;
import com.sam.project.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"用户登录相关接口"})
@Validated
@RestController
public class LoginRestController extends SuperController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtils redisUtils;

    @Resources
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ApiResponses<JSONObject> login(@RequestBody @Validated(UserPARM.Create.class) LoginPARM loginPARM) {
        JSONObject obj = userService.login(loginPARM.getLoginName(), loginPARM.getPassword(), IpUtils.getIpAddr(request));
        return success(obj);
    }

    @Resources
    @ApiOperation("用户注销")
    @PostMapping("/logout")
    public ApiResponses<Void> logout() {
        Integer uid = ApiUtils.currentUid();
        redisUtils.delete(KeyCons.USER_ACCESS_TOKEN + uid);
        return success();
    }

}

