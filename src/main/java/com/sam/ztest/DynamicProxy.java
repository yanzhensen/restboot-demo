package com.sam.ztest;

import com.sam.project.sys.service.IUserService;
import com.sam.project.sys.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 */
public class DynamicProxy implements InvocationHandler {
    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        sayBefore();
        Object result = method.invoke(target, args);
        sayAfter();
        return result;
    }

    private void sayBefore() {
        System.out.println("before...");
    }

    private void sayAfter() {
        System.out.println("after...");
    }

    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy(new UserServiceImpl());
        IUserService hello = dynamicProxy.getProxy();
        hello.sessionUser();
    }
}