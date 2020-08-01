package com.sam.framework.shiro;

import com.sam.common.utils.JWTUtils;
import com.sam.framework.cons.KeyCons;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.exception.CustomException;
import com.sam.framework.utils.ApiAssert;
import com.sam.framework.utils.LogUtils;
import com.sam.framework.utils.RedisUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * JWT Realm 适用于shiro
 *
 * @author Caratacus
 */
@Component
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //不使用shiro的session 所以不调用认证
//        Integer uid = JWTUtils.getUid(principals.toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        Set<String> roleIds = userService.getRoleIds(uid).stream().map(TypeUtils::castToString).collect(Collectors.toSet());
//        simpleAuthorizationInfo.addRoles(roleIds);
//        simpleAuthorizationInfo.addStringPermissions(resourceService.getUserPerms(uid));
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getPrincipal();
        // 判断Token是否过期
        ApiAssert.isFalse(ErrorCodeEnum.UNAUTHORIZED, JWTUtils.isExpired(token));
        Integer uid = JWTUtils.getUid(token);
        try {
            Boolean keyExists = redisUtils.hasKey(KeyCons.USER_ACCESS_TOKEN + uid);
            ApiAssert.isTrue(ErrorCodeEnum.UNAUTHORIZED, keyExists);
            //延长30分钟
            redisUtils.setEx(KeyCons.USER_ACCESS_TOKEN + uid, token, 30, TimeUnit.MINUTES);
        } catch (CustomException e) {
            LogUtils.writeLog(e.getMessage());
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
