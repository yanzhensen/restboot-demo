package com.sam.project.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.enums.MenuTypeEnum;
import com.sam.enums.StatusEnum;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import com.sam.framework.utils.TreeUtils;
import com.sam.project.sys.mapper.MenuMapper;
import com.sam.project.sys.model.dto.MenuDTO;
import com.sam.project.sys.model.dto.MenuTreeDTO;
import com.sam.project.sys.model.entity.Menu;
import com.sam.project.sys.model.entity.MenuResource;
import com.sam.project.sys.model.entity.RoleMenu;
import com.sam.project.sys.service.IMenuResourceService;
import com.sam.project.sys.service.IMenuService;
import com.sam.project.sys.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private IMenuResourceService menuResourceService;
    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(Menu menu, List<String> resourceIds) {
        save(menu);
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            Integer menuId = menu.getMid();
            //添加resource关联
            menuResourceService.saveBatch(menuResourceService.getMenuResources(menuId, resourceIds));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu, List<String> resourceIds) {
        updateById(menu);
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            Integer menuId = menu.getMid();
            //删除resource关联
            menuResourceService.removeByMenuId(menuId);
            //添加resource关联
            menuResourceService.saveBatch(menuResourceService.getMenuResources(menuId, resourceIds)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenu(Integer menuId) {
        if (parentIdNotNull(menuId)) {
            lambdaQuery().eq(Menu::getParentId, menuId)
                    .list()
                    .stream()
                    .filter(e -> parentIdNotNull(e.getParentId()))
                    .forEach(e -> removeMenu(e.getMid()));
            //删除resource关联
            menuResourceService.removeByMenuId(menuId);
            //删除菜单
            removeById(menuId);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Integer menuId, MenuTypeEnum status) {
        Menu menu = getById(menuId);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("未找到该菜单"), menu);
        Menu update = new Menu();
        update.setMid(menuId);
        update.setStatus(status);
        updateById(update);
    }


    @Override
    public MenuDTO getMenuDTODetails(Integer menuId) {
        Menu menu = getById(menuId);
        ApiAssert.notNull(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("未找到该菜单"), menu);
        MenuDTO menuDTO = menu.convert(MenuDTO.class);
        List<String> resourceIds = menuResourceService.listObjs(Wrappers.<MenuResource>lambdaQuery()
                .select(MenuResource::getResourceId)
                .eq(MenuResource::getMenuId, menuId), TypeUtils::castToString);
        menuDTO.setResourceIds(resourceIds);
        return menuDTO;
    }

    @Override
    public List<MenuTreeDTO> getUserPermMenus(Integer uid) {
        List<MenuTreeDTO> menus = baseMapper.getUserPermMenus(uid, StatusEnum.NORMAL, Arrays.asList(MenuTypeEnum.CATALOG, MenuTypeEnum.MENU));
        return menus.stream().filter(e -> !parentIdNotNull(e.getParentId())).map(e -> TreeUtils.findChildren(e, menus)).collect(Collectors.toList());
    }

    /**
     * 父ID不为0并且不为空
     *
     * @param parentId
     * @return
     */
    private boolean parentIdNotNull(Integer parentId) {
        return Objects.nonNull(parentId) && parentId != 0;
    }

    @Override
    public JSONObject getUserPermButtonAliases(Integer uid) {
        List<MenuTreeDTO> menuDTOList = baseMapper.getUserPermMenus(uid, StatusEnum.NORMAL, Collections.singletonList(MenuTypeEnum.BUTTON));
        JSONObject obj = new JSONObject();
        for (MenuTreeDTO menuTreeDTO : menuDTOList) {
            obj.put(menuTreeDTO.getAlias(), true);
        }
        return obj;
    }

    @Override
    public JSONObject getUserObjMenus(Integer uid) {
        List<MenuTreeDTO> menus = baseMapper.getUserPermMenus(uid, StatusEnum.NORMAL, Arrays.asList(MenuTypeEnum.CATALOG, MenuTypeEnum.MENU));
        JSONObject obj = new JSONObject();
        for (MenuTreeDTO menuTreeDTO : menus) {
            if (Objects.nonNull(menuTreeDTO.getRouter())) {
                obj.put(menuTreeDTO.getRouter(), true);
            }
        }
        return obj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accessMenu(Integer roleId, List<Integer> menuIds) {
        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
        List<RoleMenu> array = new ArrayList<>();
        for (Integer id : menuIds) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(id);
            array.add(rm);
        }
        roleMenuService.saveBatch(array);
    }

    @Override
    public List<MenuTreeDTO> getTreeAllPermMenus() {
        List<MenuTreeDTO> menus = list().stream().map(e -> e.convert(MenuTreeDTO.class)).collect(Collectors.toList());
        return menus.stream().filter(e -> !parentIdNotNull(e.getParentId())).map(e -> TreeUtils.findChildren(e, menus)).collect(Collectors.toList());
    }
}
