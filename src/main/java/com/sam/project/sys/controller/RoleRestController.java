package com.sam.project.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.project.sys.model.entity.Role;
import com.sam.project.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"角色相关接口"})
@Validated
@RestController
@RequestMapping("/roles")
public class RoleRestController extends SuperController {

    @Autowired
    private IRoleService roleService;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "分页所有角色", notes = "<br/>Sam2020-07-27：新增接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cursor", value = "当前页数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "角色名 模糊", paramType = "query"),
    })
    @GetMapping
    public ApiResponses<IPage<Role>> page(@RequestParam(value = "name", required = false) String name) {
        IPage<Role> page = roleService.page(this.getPage(), Wrappers.<Role>lambdaQuery().eq(StringUtils.isNotEmpty(name), Role::getRoleName, name));
        return success(page);
    }
}

