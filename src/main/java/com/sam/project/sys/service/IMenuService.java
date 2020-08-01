package com.sam.project.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.enums.MenuTypeEnum;
import com.sam.project.sys.model.dto.MenuDTO;
import com.sam.project.sys.model.dto.MenuTreeDTO;
import com.sam.project.sys.model.entity.Menu;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 保存菜单
     *
     * @param menu
     * @param resourceIds
     */
    void saveMenu(Menu menu, List<String> resourceIds);

    /**
     * 修改菜单
     *
     * @param menu
     * @param resourceIds
     */
    void updateMenu(Menu menu, List<String> resourceIds);

    /**
     * 递归删除菜单
     *
     * @param menuId
     */
    void removeMenu(Integer menuId);

    /**
     * 修改菜单状态
     *
     * @param menuId
     * @param status
     */
    void updateStatus(Integer menuId, MenuTypeEnum status);

    /**
     * 获取菜单详情
     *
     * @param menuId
     * @return
     */
    MenuDTO getMenuDTODetails(Integer menuId);

    /**
     * 获取用户权限菜单 返回树
     *
     * @param uid
     * @return
     */
    List<MenuTreeDTO> getUserPermMenus(Integer uid);

    /**
     * 获取用户按钮权限
     *
     * @param uid
     * @return
     */
    JSONObject getUserPermButtonAliases(Integer uid);

    /**
     * 获取用户按钮权限
     *
     * @param uid
     * @return
     */
    JSONObject getUserObjMenus(Integer uid);

    /**
     * 授权角色菜单
     *
     * @param roleId
     * @param menuIds
     */
    void accessMenu(Integer roleId, List<Integer> menuIds);

    /**
     * 获取所有菜单 返回树
     *
     * @return
     */
    List<MenuTreeDTO> getTreeAllPermMenus();
}
