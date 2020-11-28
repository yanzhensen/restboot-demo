package com.sam.project.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sam.framework.config.CacheConfig;
import com.sam.framework.utils.EhCacheUtils;
import com.sam.project.sys.mapper.UserMapper;
import com.sam.project.sys.model.entity.User;
import com.sam.project.sys.service.ICacheTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheTestServiceImpl implements ICacheTestService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private EhCacheUtils ehCacheUtils;

    @Override
    public User getUserById(String id) {
        String key = this.getClass().getName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName()
                + "-id:" + id;
        System.out.println(key);
        // 操作缓存
        User cache = (User) ehCacheUtils.get(CacheConfig.EhCacheNames.CACHE_10MINS, key);
        System.out.println("获取缓存:" + cache);
        User u = null;
        if (cache == null) {
            log.info("ehcache 没有");
            String userRedis = (String) redisTemplate.opsForValue().get(key);
            if (StringUtils.isEmpty(userRedis)) {
                log.info("redis 没有");
                User user = userMapper.selectById(id);
                if (user != null) {
                    u = user;
                    ehCacheUtils.put(CacheConfig.EhCacheNames.CACHE_10MINS, key, u);
                    redisTemplate.opsForValue().set(key, JSON.toJSONString(u), 300);
                }
            } else {
                log.info("redis 找到");
                u = JSON.parseObject(userRedis, User.class);
                ehCacheUtils.put(CacheConfig.EhCacheNames.CACHE_10MINS, key, u);
            }
        } else {
            log.info("ehcache 找到");
            u = cache;
        }
        return u;
    }

    /**
     * 单独使用redis缓存, 指定key
     * 指定缓存 cacheManager
     * 指定缓存组 value
     *
     * @return
     */
    @Cacheable(key = "'user_' + #id", cacheManager = CacheConfig.CacheManagerNames.REDIS_CACHE_MANAGER, value = CacheConfig.RedisCacheNames.CACHE_15MINS)
    @Override
    public User getUserFromRedisHasKey(Integer id) {
        String userData = (String) redisTemplate.opsForValue().get("userOp_" + id);
        User user = null;
        if (StringUtils.isEmpty(userData)) {
            log.info("redis 没有");
            user = userMapper.selectById(id);
            redisTemplate.opsForValue().set("userOp_" + id, JSONObject.toJSONString(user), 300, TimeUnit.SECONDS);
        } else {
            log.info("redis 找到");
            user = JSONObject.parseObject(userData, User.class);
        }
        return user;
    }

    /**
     * 单独使用redis缓存, 不指定key
     * 指定缓存 cacheManager
     * 指定缓存组 value
     *
     * @return
     */
    @Cacheable(cacheManager = CacheConfig.CacheManagerNames.REDIS_CACHE_MANAGER, value = CacheConfig.RedisCacheNames.CACHE_15MINS)
    @Override
    public User getUserFromRedisNoKey(Integer id) {
        String userData = (String) redisTemplate.opsForValue().get("userOp_" + id);
        User user = null;
        if (StringUtils.isEmpty(userData)) {
            log.info("redis 没有");
            user = userMapper.selectById(id);
            System.out.println("JSONObject.toJSONString(user) = " + JSONObject.toJSONString(user));
            redisTemplate.opsForValue().set("userOp_" + id, JSONObject.toJSONString(user), 300, TimeUnit.SECONDS);
        } else {
            log.info("redis 找到");
            user = JSONObject.parseObject(userData, User.class);
        }
        return user;
    }

    @CachePut(key = "'user_' + #id", cacheManager = CacheConfig.CacheManagerNames.REDIS_CACHE_MANAGER, value = CacheConfig.RedisCacheNames.CACHE_15MINS)
    @Override
    public User putCacheRedis(Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @CacheEvict(key = "'user_' + #id", cacheManager = CacheConfig.CacheManagerNames.REDIS_CACHE_MANAGER, value = CacheConfig.RedisCacheNames.CACHE_15MINS)
    @Override
    public String delCacheRedis(Integer id) {
        redisTemplate.delete("userOp_" + id);
        return "OK";
    }


    /**
     * 使用ehcache 存储
     *
     * @param key
     * @return
     */
    @Override
    @Cacheable(key = "'key'", cacheManager = CacheConfig.CacheManagerNames.EHCACHE_CACHE_MAANGER, cacheNames = CacheConfig.EhCacheNames.CACHE_10MINS)
    public String demo1(String key) {
        System.out.println("......demo1");
        return "abc" + key;
    }

    /**
     * redis 存储
     *
     * @param key
     * @return
     */
    @Override
    @Cacheable(key = "'key'", cacheManager = CacheConfig.CacheManagerNames.REDIS_CACHE_MANAGER, cacheNames = CacheConfig.RedisCacheNames.CACHE_15MINS)
    public String demo2(String key) {
        System.out.println("......demo2");
        return "abcdemo2" + key;
    }
}
