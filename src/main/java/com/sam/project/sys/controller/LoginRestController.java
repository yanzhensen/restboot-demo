package com.sam.project.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.sam.common.annotations.Resources;
import com.sam.common.utils.ApiUtils;
import com.sam.common.utils.IpUtils;
import com.sam.common.utils.JWTUtils;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.cons.KeyCons;
import com.sam.framework.controller.SuperController;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.ApiAssert;
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

    @Resources(auth = AuthTypeEnum.LOGIN)
    @ApiOperation("用户注销")
    @PostMapping("/logout")
    public ApiResponses<Void> logout() {
        Integer uid = ApiUtils.currentUid();
        redisUtils.delete(KeyCons.USER_REFRESH_TOKEN + uid);
        return success();
    }

    @Resources(auth = AuthTypeEnum.LOGIN)
    @ApiOperation(value = "token刷新", notes = "一定要携带没过期的token才能获取新的token")
    @PostMapping("/refresh")
    public ApiResponses<String> refresh() {
        Integer uid = ApiUtils.currentUid();
        String refreshToken = redisUtils.get(KeyCons.USER_REFRESH_TOKEN + uid);
        //没有refreshToken或者解析的refreshToken过期了 都要重新登录
        boolean isExpired = JWTUtils.isExpired(refreshToken);
        ApiAssert.isFalse(ErrorCodeEnum.UNAUTHORIZED, isExpired);
        String newToken = JWTUtils.generate(uid);
        return success(newToken);
    }

}

