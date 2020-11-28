package com.sam.project.sys.service;

import com.sam.project.sys.model.entity.User;

public interface ICacheTestService {
    /**
     * 添加缓存
     *
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 指定key 使用redis缓存
     *
     * @param id
     * @return
     */
    User getUserFromRedisHasKey(Integer id);

    /**
     * 不指定key 使用redis缓存
     *
     * @param id
     * @return
     */
    User getUserFromRedisNoKey(Integer id);

    /**
     * 添加缓存
     *
     * @param id
     * @return
     */
    User putCacheRedis(Integer id);

    /**
     * 删除redis缓存
     *
     * @param id
     * @return
     */
    String delCacheRedis(Integer id);

    String demo1(String key);

    String demo2(String key);
}
