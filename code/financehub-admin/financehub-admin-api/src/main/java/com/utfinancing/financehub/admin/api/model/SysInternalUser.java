package com.utfinancing.financehub.admin.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <ul>
 * <li>Project :  financehub-admin</li>
 * <li>ClassName : com.utfinancing.financehub.admin.api.model.SysInternalUser</li>
 * <li>CreateTime : 2024/01/09 11:12</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class SysInternalUser {

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;
}
