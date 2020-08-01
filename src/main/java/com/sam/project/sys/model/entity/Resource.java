package com.sam.project.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sam.framework.model.convert.Convert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_resource")
@ApiModel(value = "Resource对象", description = "资源表")
public class Resource extends Convert {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String resouId;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "路径映射")
    private String mapping;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "权限认证类型")
    private Integer authType;

    @ApiModelProperty(value = "权限标识")
    private String perm;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
