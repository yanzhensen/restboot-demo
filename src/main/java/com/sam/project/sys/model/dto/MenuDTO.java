package com.sam.project.sys.model.dto;

import com.sam.enums.MenuTypeEnum;
import com.sam.framework.model.convert.Convert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

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
@ApiModel(value = "MenuDTO对象", description = "菜单表")
public class MenuDTO extends Convert {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(notes = "关联资源ID")
    private List<String> resourceIds;


}
