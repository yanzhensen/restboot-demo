package com.sam.project.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.cons.KeyCons;
import com.sam.framework.utils.RedisUtils;
import com.sam.project.sys.mapper.ResourceMapper;
import com.sam.project.sys.model.dto.ResourcePermDTO;
import com.sam.project.sys.model.entity.Resource;
import com.sam.project.sys.model.entity.RoleResource;
import com.sam.project.sys.service.IResourceService;
import com.sam.project.sys.service.IRoleResourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private IRoleResourceService roleResourceService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<String> getUserPerms(Integer uid) {
        return getUserResourcePerms(uid).stream().map(e -> this.getResourcePermTag(e.getMethod(), e.getMapping())).collect(Collectors.toList());
    }

    @Override
    public List<ResourcePermDTO> getOpenPerms() {
        List<ResourcePermDTO> openPerms = new ArrayList<>();
        //经常调用走缓存
        String openStr = null;
        openStr = redisUtils.get(KeyCons.OPEN_PERMS);
        if (StringUtils.isNotEmpty(openStr)) {
            try {
                openPerms = JSONArray.parseArray(openStr, ResourcePermDTO.class);
                return openPerms;
            } catch (Exception e) {
                //解析失败了，重新查询
            }
        }
        openPerms = getPerms(AuthTypeEnum.OPEN);
        redisUtils.setEx(KeyCons.OPEN_PERMS, JSON.toJSONString(openPerms), 30, TimeUnit.MINUTES);
        return openPerms;
    }

    @Override
    public List<ResourcePermDTO> getLoginPerms() {
        return getPerms(AuthTypeEnum.LOGIN);
    }

    @Override
    public List<ResourcePermDTO> getPerms(AuthTypeEnum... authTypes) {
//        List<Integer> auths = Arrays.asList(authTypes).stream().map(e -> e.getValue()).collect(Collectors.toList());
        List<Resource> list = this.lambdaQuery().select(Resource::getMethod, Resource::getMapping)
//                .in(CollectionUtils.isNotEmpty(auths), Resource::getAuthType, auths).list();
                .in(ArrayUtils.isNotEmpty(authTypes), Resource::getAuthType, authTypes).list();
        return list.stream().map(e -> e.convert(ResourcePermDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Set<ResourcePermDTO> getUserResourcePerms(Integer uid) {
        List<ResourcePermDTO> perms = new LinkedList<>();
        String userPerms = redisUtils.get(KeyCons.USER_PERMS + uid);
        if (StringUtils.isNotEmpty(userPerms)) {
            try {
                perms = JSONArray.parseArray(userPerms, ResourcePermDTO.class);
                return new HashSet<>(perms);
            } catch (Exception e) {
                //解析失败了，重新查询
            }
        }
        //获取用户权限 并设置缓存
        List<ResourcePermDTO> openPerms = getPerms(AuthTypeEnum.OPEN, AuthTypeEnum.LOGIN);
        List<ResourcePermDTO> resourcePerms = baseMapper.getUserResourcePerms(uid);
        List<ResourcePermDTO> userMenuResourcePerms = getUserMenuResourcePerms(uid);
        perms.addAll(openPerms);
        perms.addAll(resourcePerms);
        perms.addAll(userMenuResourcePerms);
        redisUtils.setEx(KeyCons.USER_PERMS + uid, JSON.toJSONString(perms), 30, TimeUnit.MINUTES);
        return new HashSet<>(perms);
    }

    @Override
    public List<ResourcePermDTO> getResourcePerms(String method) {
        String allResource = redisUtils.get(KeyCons.ALL_RESOURCE);
        if (StringUtils.isNotEmpty(allResource)) {
            try {
                List<ResourcePermDTO> allList = JSONArray.parseArray(allResource, ResourcePermDTO.class);
                return allList.stream().filter(e -> e.getMethod().equals(method)).collect(Collectors.toList());
            } catch (Exception e) {
                //解析失败了，重新查询
            }
        }
        List<ResourcePermDTO> all = list().stream().map(a -> a.convert(ResourcePermDTO.class)).collect(Collectors.toList());
        redisUtils.setEx(KeyCons.ALL_RESOURCE, JSON.toJSONString(all), 30, TimeUnit.MINUTES);
        return all.stream().filter(b -> b.getMethod().equals(method)).collect(Collectors.toList());
    }

    @Override
    public String getResourcePermTag(String method, String mapping) {
        return method + ":" + mapping;
    }


    @Override
    public List<ResourcePermDTO> getUserMenuResourcePerms(Integer uid) {
        return baseMapper.getUserMenuResourcePerms(uid);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<Resource> entityList) {
        int batchSize = 1024;
        //批量对象插入 不存在直接返回true
        if (CollectionUtils.isEmpty(entityList)) {
            return true;
        }
        List<String> resources = list(Wrappers.<Resource>lambdaQuery().select(Resource::getResouId))
                .stream().map(Resource::getResouId).collect(Collectors.toList());
        int i = 0;
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            for (Resource entity : entityList) {
                if (resources.contains(entity.getResouId())) {
                    entity.setUpdateTime(LocalDateTime.now());
                    MapperMethod.ParamMap<Resource> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                } else {
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                }
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accessResource(Integer roleId, List<String> resourceIds) {
        roleResourceService.remove(Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getRoleId, roleId));
        List<RoleResource> array = new ArrayList<>();
        for (String id : resourceIds) {
            RoleResource rt = new RoleResource();
            rt.setRoleId(roleId);
            rt.setResourceId(id);
            array.add(rt);
        }
        roleResourceService.saveBatch(array);
    }


}
