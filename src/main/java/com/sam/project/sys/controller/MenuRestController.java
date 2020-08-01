package com.sam.project.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.enums.MenuTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.TreeUtils;
import com.sam.project.sys.model.dto.MenuDTO;
import com.sam.project.sys.model.dto.MenuTreeDTO;
import com.sam.project.sys.model.entity.Menu;
import com.sam.project.sys.model.parm.MenuPARM;
import com.sam.project.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"菜单相关接口"})
@RestController
@RequestMapping("/menus")
public class MenuRestController extends SuperController {

    @Autowired
    private IMenuService menuService;

    @Resources(auth = AuthTypeEnum.LOGIN)
    @ApiOperation("获取用户权限菜单 返回树")
    @GetMapping
    public ApiResponses<List<MenuTreeDTO>> get() {
        List<MenuTreeDTO> menuTrees = menuService.getUserPermMenus(currentUid());
        return success(menuTrees);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("获取所有菜单 返回树")
    @GetMapping("/trees")
    public ApiResponses<List<MenuTreeDTO>> menus() {
        List<MenuTreeDTO> menuTrees = menuService.getTreeAllPermMenus();
        return success(menuTrees);
    }

    @ApiOperation("获取用户按钮权限")
    @GetMapping("/obj")
    public ApiResponses<JSONObject> menusObj() {
        return success(menuService.getUserObjMenus(currentUid()));
    }

    @Resources(auth = AuthTypeEnum.LOGIN)
    @ApiOperation("获取用户按钮权限")
    @GetMapping("/buttons/aliases")
    public ApiResponses<JSONObject> buttonsAliases() {
        return success(menuService.getUserPermButtonAliases(currentUid()));
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/list")
    public ApiResponses<List<Menu>> list() {
        return success(menuService.list());
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询单个菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, paramType = "path")
    })
    @GetMapping("/{id}")
    public ApiResponses<MenuDTO> get(@PathVariable("id") Integer id) {
        return success(menuService.getMenuDTODetails(id));
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "添加菜单")
    @PostMapping
    public ApiResponses<Void> create(@RequestBody @Validated(MenuPARM.Create.class) MenuPARM menuPARM) {
        Menu menu = menuPARM.convert(Menu.class);
        menuService.saveMenu(menu, menuPARM.getResourceIds());
        return success(HttpStatus.CREATED);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "修改菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, paramType = "path")
    })
    @PutMapping("/{id}")
    public ApiResponses<Void> update(@PathVariable("id") Integer id, @RequestBody @Validated(MenuPARM.Update.class) MenuPARM menuPARM) {
        Menu menu = menuPARM.convert(Menu.class);
        menu.setMid(id);
        menuService.updateMenu(menu, menuPARM.getResourceIds());
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public ApiResponses<Void> delete(@PathVariable("id") Integer id) {
        menuService.removeMenu(id);
        return success(HttpStatus.NO_CONTENT);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation("设置菜单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, paramType = "path")
    })
    @PutMapping("/{id}/status")
    public ApiResponses<Void> updateStatus(@PathVariable("id") Integer id, @RequestParam("status") MenuTypeEnum status) {
        menuService.updateStatus(id, status);
        return success();
    }

    @Resources
    @ApiOperation(value = "授权菜单")
    @PostMapping("/access")
    public ApiResponses<Void> access() {
        List<Integer> menuIds = menuService.list().stream().map(e -> e.getMid()).collect(Collectors.toList());
        menuService.accessMenu(1, menuIds);
        return success();
    }
}

