package com.sam.project.sys.model.dto;

import com.sam.enums.StatusEnum;
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
 * 系统用户表
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "UserDTO对象", description = "系统用户表")
public class UserDTO extends Convert {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "IP地址")
    private String loginIp;

    @ApiModelProperty(value = "登陆名")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "状态 0：禁用 1：正常")
    private StatusEnum status;

    @ApiModelProperty(value = "创建者ID")
    private Integer createUid;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(notes = "用户角色ID")
    private List<Integer> roleIds;

    @ApiModelProperty(notes = "头像")
    private String phoHead;


}
