package com.sam.framework.cons;

/**
 * Redis key常量
 */
public interface KeyCons {
    /**
     * 用户登录token
     */
    String USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN_";
    /**
     * 后台所有权限
     */
    String ALL_RESOURCE = "USER_ALL_RESOURCE";
    /**
     * 后台所有公开权限
     */
    String OPEN_PERMS = "USER_OPEN_PERMS";
    /**
     * 后台用户权限
     */
    String USER_PERMS = "USER_PERMS_";
}
