package com.sam.framework.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.sam.common.utils.JWTUtils;
import com.sam.framework.utils.RequestUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据token 返回co
 *
 * @author Sam
 */
public class DsAuthorizationProcessor extends DsProcessor {
    private static final String HEADER_PREFIX = "#authorization";

    public DsAuthorizationProcessor() {
    }

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = RequestUtils.getToken(request);
        return JWTUtils.getCo(token);
    }
}
