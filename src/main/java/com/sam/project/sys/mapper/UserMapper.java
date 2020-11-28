package com.sam.project.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sam.project.sys.model.entity.User;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
//启用二级缓存
//@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface UserMapper extends BaseMapper<User> {

}
