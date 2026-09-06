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
 * @Description :   SysInternalRoleUser查询from对象
 * @Modified :
 */
@ApiModel("SysInternalRoleUser查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysInternalRoleUserQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "内部角色Id")
    private Long roleId;

    @ApiModelProperty(value = "内部用户Id")
    private Long sysUserId;

    @ApiModelProperty(value = "是否生效（0：生效，1：失效）")
    private String status;

    @ApiModelProperty(value = "内部用户名称")
    private String userName;
}
