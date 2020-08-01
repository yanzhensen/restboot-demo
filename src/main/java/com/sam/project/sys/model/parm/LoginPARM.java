package com.sam.project.sys.model.parm;

import com.sam.framework.model.convert.Convert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 登陆参数
 * </p>
 *
 * @author Caratacus
 */
@ApiModel
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginPARM extends Convert {

    @NotBlank(message = "用户名不能为空！")
    @ApiModelProperty(notes = "账号")
    private String loginName;

    @NotBlank(message = "密码不能为空！")
    @ApiModelProperty(notes = "密码")
    private String password;

}
