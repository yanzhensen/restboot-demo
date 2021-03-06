package com.sam.project.info.model.entity;

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
 * 图片表
 * </p>
 *
 * @author Sam
 * @since 2020-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("info_photo")
@ApiModel(value = "Photo对象", description = "图片表")
public class Photo extends Convert {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片ID")
    @TableId(value = "photo_id", type = IdType.AUTO)
    private Integer photoId;

    @ApiModelProperty(value = "外表ID")
    private Integer photoTableId;

    @ApiModelProperty(value = "外表类型")
    private String photoTableType;

    @ApiModelProperty(value = "随机数")
    private String photoRandom;

    @ApiModelProperty(value = "文件类型")
    private String photoMimeType;

    @ApiModelProperty(value = "图片路径")
    private String photoUrl;

    @ApiModelProperty(value = "图片名称")
    private String photoName;

    @ApiModelProperty(value = "存储空间类型")
    private String photoBucket;

    @ApiModelProperty(value = "图片状态")
    private String photoStatus;

    @ApiModelProperty(value = "优先级")
    private Integer photoIndex;

    @ApiModelProperty(value = "上传人ID")
    private Integer photoUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime photoCreateTime;


}
