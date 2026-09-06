package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-07
 * @Description : 出租登记DTO对象
 * @Modified :
 */
@Data
public class RentRegisterDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "房产租赁合同编号")
    private String contractCode;

    @ApiModelProperty(value = "资产编号（存在多个用,隔开）")
    private String assetNumber;

    @ApiModelProperty(value = "转出时间")
    private LocalDate transferOutDate;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "租金总额")
    private BigDecimal rentTotal;

    @ApiModelProperty(value = "租赁保证金")
    private BigDecimal rentBond;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "版本号")
    private Integer versionNum;
}
