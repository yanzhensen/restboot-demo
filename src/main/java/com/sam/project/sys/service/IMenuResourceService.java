package com.sam.project.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.project.sys.model.entity.MenuResource;

import java.util.List;

/**
 * <p>
 * 菜单资源关系表 服务类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface IMenuResourceService extends IService<MenuResource> {

    /**
     * 根据菜单ID删除资源关系记录
     *
     * @param menuId
     */
    void removeByMenuId(Integer menuId);

    /**
     * 获取菜单资源关系
     *
     * @param menuId
     * @param resourceIds
     */
    List<MenuResource> getMenuResources(Integer menuId, List<String> resourceIds);
}
