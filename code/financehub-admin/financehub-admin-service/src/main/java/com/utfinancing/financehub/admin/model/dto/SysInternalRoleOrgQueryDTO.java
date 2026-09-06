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
 * @Date : Create in 2023-11-16
 * @Description :   SysInternalRoleOrg查询from对象
 * @Modified :
 */
@ApiModel("SysInternalRoleOrg查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysInternalRoleOrgQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约实体")
    private String orgId;

    @ApiModelProperty(value = "签约实体名称")
    private String orgName;

    @ApiModelProperty(value = "系统内部角色id")
    private Long roleId;
}
