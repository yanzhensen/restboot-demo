package com.sam.framework.aspect;

import com.alibaba.fastjson.JSON;
import com.sam.framework.utils.LogUtils;
import com.sam.framework.utils.RequestUtils;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Controller统一切点日志处理
 */
@Aspect
public class LogRecordAspect {


    @Pointcut("execution(public * com.sam.project.*.*.*RestController.*(..))")
    @SuppressWarnings("EmptyMethod")
    public void pointCut() {
    }

    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void doAfterReturning(Object ret) {
        LogUtils.doAfterReturning(ret);
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String token = RequestUtils.getToken(request);
        //请求域名
        String serverName = request.getServerName();
        //请求方式
        String requestMethod = request.getMethod();
        //请求项目路径
        String uri = request.getRequestURI();
        //POST请求参数
        Object[] args = point.getArgs();
        //GET请求参数
        String queryString = request.getQueryString();
        String params = "";
        if (args.length > 0) {
            if ("POST".equals(requestMethod)) {
                Object object = args[0];
                params = JSON.toJSONString(object);
            } else if ("GET".equals(requestMethod)) {
                params = queryString;
            } else if ("PUT".equals(requestMethod)) {
                Object object;
                if (args.length >= 2) {
                    object = args[1];
                } else {
                    object = args[0];
                }
                params = JSON.toJSONString(object);
            } else if ("DELETE".equals(requestMethod)) {
                if (Objects.nonNull(queryString)) {
                    params = queryString;
                }
            }
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method signatureMethod = signature.getMethod();
        ApiOperation annotation = signatureMethod.getAnnotation(ApiOperation.class);
        String annotationValue = null;
        if (annotation != null) {
            //注解上的描述
            annotationValue = annotation.value();
        }
        //请求IP 需要前端传
        String ipAddress = "";
//        if (Objects.equals(uri, "/demo/login")) {
//            ipAddress = JSON.parseObject(params).getString("ipAddress");
//        } else if (Objects.equals(uri, "/demo/getCo")) {
//            String[] paramArr = params.split("&amp;");
//            for (int i = 0; i < paramArr.length; i++) {
//                String[] str = paramArr[i].split("=");
//                if (Objects.equals(str[0], "ipAddress")) {
//                    ipAddress = str[i];
//                }
//            }
//        }
        long beginTime = System.currentTimeMillis();
        //执行接口方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long expendTime = System.currentTimeMillis() - beginTime;
        System.out.println("执行：" + uri + " 方式：" + requestMethod + "（" + annotationValue + "） 接口耗时 = " + expendTime + " ip：" + ipAddress);
        return result;
    }
}
