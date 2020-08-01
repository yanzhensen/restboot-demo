package com.sam.project.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.enums.AuthTypeEnum;
import com.sam.project.sys.model.dto.ResourcePermDTO;
import com.sam.project.sys.model.entity.Resource;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 根据用户ID获取用户所有权限
     *
     * @param uid
     * @return 返回method + ":" + mapping List集合
     */
    List<String> getUserPerms(Integer uid);

    /**
     * 获取开放权限资源列表
     * 先走缓存 没有在查数据库
     *
     * @return
     */
    List<ResourcePermDTO> getOpenPerms();

    /**
     * 获取需要登录权限资源列表
     *
     * @return
     */
    List<ResourcePermDTO> getLoginPerms();

    /**
     * 获取指定类型权限资源列表
     *
     * @param authTypes 类型
     * @return
     */
    List<ResourcePermDTO> getPerms(AuthTypeEnum... authTypes);

    /**
     * 获取用户所有权限 设置缓存
     *
     * @param uid
     * @return
     */
    Set<ResourcePermDTO> getUserResourcePerms(Integer uid);

    /**
     * 获取某种请求方式资源权限
     *
     * @param method
     * @return
     */
    List<ResourcePermDTO> getResourcePerms(String method);

    /**
     * 获取资源权限标记
     *
     * @param method
     * @param mapping
     * @return method + ":" + mapping
     */
    String getResourcePermTag(String method, String mapping);

    /**
     * 获取用户菜单资源权限
     *
     * @param uid
     * @return
     */
    List<ResourcePermDTO> getUserMenuResourcePerms(Integer uid);

    /**
     * 授权角色资源
     *
     * @param roleId
     * @param resourceIds
     */
    void accessResource(Integer roleId, List<String> resourceIds);
}
