package com.utfinancing.financehub.admin.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description : 中台内部角色和签约实体关联表DTO对象
 * @Modified :
 */
@Data
public class SysInternalRoleOrgDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "签约实体")
    private String orgId;

    @ApiModelProperty(value = "签约实体名称")
    private String orgName;

    @ApiModelProperty(value = "系统内部角色id")
    private Long roleId;

    @ApiModelProperty(value = "是否删除（0：否：1：是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
