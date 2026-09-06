package com.utfinancing.financehub.admin.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :   SysInternalUser查询from对象
 * @Modified :
 */
@ApiModel("SysInternalUser查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysInternalUserQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "角色Id")
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
