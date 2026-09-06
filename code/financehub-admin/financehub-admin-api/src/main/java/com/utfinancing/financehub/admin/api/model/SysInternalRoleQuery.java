package com.utfinancing.financehub.admin.api.model;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description :   SysInternalRole查询from对象
 * @Modified :
 */
@ApiModel("SysInternalRole查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysInternalRoleQuery extends BaseQueryDTO{

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "是否生效（0：生效，1：不生效）")
    private String status;
}
