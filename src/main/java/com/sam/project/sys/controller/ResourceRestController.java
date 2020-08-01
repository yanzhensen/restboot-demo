package com.sam.project.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.project.sys.model.entity.Resource;
import com.sam.project.sys.service.IResourceService;
import com.sam.project.sys.service.ScanMappings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Validated
@Api(tags = {"资源操作相关接口"})
@RestController
@RequestMapping("/resources")
public class ResourceRestController extends SuperController {

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private ScanMappings scanMappings;

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询所有资源(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", value = "资源名 模糊", paramType = "query"),
            @ApiImplicitParam(name = "method", value = "请求方式 精确", paramType = "query"),
            @ApiImplicitParam(name = "authType", value = "权限认证类型 精确", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<Resource>> page(@RequestParam(value = "resourceName", required = false) String resourceName,
                                              @RequestParam(value = "method", required = false) String method,
                                              @RequestParam(value = "authType", required = false) AuthTypeEnum authType) {
        IPage<Resource> resourceIPage = resourceService.page(this.getPage(), Wrappers.<Resource>lambdaQuery()
                .like(StringUtils.isNotEmpty(resourceName), Resource::getResourceName, resourceName)
                .eq(StringUtils.isNotEmpty(method), Resource::getMethod, method)
                .eq(Objects.nonNull(authType), Resource::getAuthType, authType));
        return success(resourceIPage);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "查询所有资源")
    @GetMapping("/resources")
    public ApiResponses<List<Resource>> list() {
        return success(resourceService.list());
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "刷新资源")
    @PutMapping
    public ApiResponses<Void> refresh() {
        scanMappings.doScan();
        return success();
    }

    @Resources
    @ApiOperation(value = "管理员授权所有资源")
    @PostMapping("/access")
    public ApiResponses<Void> access() {
        List<String> resourceIds = resourceService.list().stream().map(e -> e.getResouId()).collect(Collectors.toList());
        resourceService.accessResource(1, resourceIds);
        return success();
    }
}

