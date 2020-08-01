package com.sam.project.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.project.sys.mapper.MenuResourceMapper;
import com.sam.project.sys.model.entity.MenuResource;
import com.sam.project.sys.service.IMenuResourceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单资源关系表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Service
public class MenuResourceServiceImpl extends ServiceImpl<MenuResourceMapper, MenuResource> implements IMenuResourceService {

    @Override
    public void removeByMenuId(Integer menuId) {
        remove(Wrappers.<MenuResource>lambdaQuery().eq(MenuResource::getMenuId, menuId));
    }

    @Override
    public List<MenuResource> getMenuResources(Integer menuId, List<String> resourceIds) {
        return resourceIds.stream().map(resourceId -> new MenuResource(menuId, resourceId)).collect(Collectors.toList());
    }
}
