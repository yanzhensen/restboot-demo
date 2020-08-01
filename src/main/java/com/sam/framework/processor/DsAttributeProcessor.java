package com.sam.framework.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过页面参数(request.setAttribute进行注入) 用DS(#attribute.参数)来获取参数
 *
 * @author Sam
 */
public class DsAttributeProcessor extends DsProcessor {

    private static final String HEADER_ATTRIBUTE_PREFIX = "#attribute";

    public DsAttributeProcessor() {
    }

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_ATTRIBUTE_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String attribute = request.getAttribute(key.substring(11)).toString();
        return attribute;
    }
}
