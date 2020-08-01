package com.sam.framework.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过页面参数(request.setParameter进行注入) 用DS(#param.参数)来获取参数
 *
 * @author Sam
 */
public class DsParameterProcessor extends DsProcessor {
    private static final String HEAD_PARAMETER_PREFIX = "#param";

    public DsParameterProcessor() {
    }

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEAD_PARAMETER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(key.substring(7));
    }
}
