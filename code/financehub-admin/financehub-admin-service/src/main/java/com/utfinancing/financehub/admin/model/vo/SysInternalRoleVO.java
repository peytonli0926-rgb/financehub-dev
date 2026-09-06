package com.utfinancing.financehub.admin.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description : 中台内部角色表VO对象
 * @Modified :
 */
@Data
public class SysInternalRoleVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "是否生效（0：生效，1：不生效）")
    private String status;

    @ApiModelProperty(value = "是否删除(0:未删除，1：已删除)")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "用户id")
    private Long sysUserId;

}
