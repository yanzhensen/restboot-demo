package com.sam.project.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.project.sys.model.entity.RoleMenu;
import com.sam.project.sys.service.IRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单关系表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"角色菜单关系相关接口"})
@Validated
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuRestController extends SuperController {

    @Autowired
    private IRoleMenuService roleMenuService;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询授权角色菜单Id", notes = "<br/>Sam2020-07-27：新增接口")
    @GetMapping("/{id}")
    public ApiResponses<List<Integer>> get(@PathVariable("id") Integer id) {
        return success(roleMenuService.list(Wrappers.<RoleMenu>lambdaQuery()
                .eq(RoleMenu::getRoleId, id)).stream().map(e -> e.getMenuId()).collect(Collectors.toList()));
    }
}

