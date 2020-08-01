package com.sam.project.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sam.enums.MenuTypeEnum;
import com.sam.framework.model.convert.Convert;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu extends Convert {

    private static final long serialVersionUID=1L;

    @TableId(value = "mid", type = IdType.AUTO)
    private Integer mid;

    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "类型:0:目录,1:菜单,2:按钮")
    private Integer menuType;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "路由")
    private String router;

    @ApiModelProperty(value = "别名")
    private String alias;

    @ApiModelProperty(value = "状态 0：禁用 1：正常")
    private MenuTypeEnum status;

    @ApiModelProperty(value = "创建者ID")
    private Integer createUid;

    @ApiModelProperty(value = "修改者ID")
    private Integer updateUid;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
