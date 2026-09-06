package com.utfinancing.financehub.admin.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description : 中台内部用户上级和下级关联表DTO对象
 * @Modified :
 */
@Data
public class SysInternalSubUserDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "上级内部用户id")
    private Long parentUserId;

    @ApiModelProperty(value = "下级用户Id")
    private Long subUserId;

    @ApiModelProperty(value = "是否删除（0：否，1：是）")
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
