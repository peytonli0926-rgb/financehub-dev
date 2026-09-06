package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description : 服务费计划表DTO对象
 * @Modified :
 */
@Data
public class ServiceFeePlanDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;

    @ApiModelProperty(value = "服务费摊销利率")
    private BigDecimal serviceFeeAmortizationRate;

    @ApiModelProperty(value = "实际计提金额")
    private BigDecimal actualAccruedAmount;

    @ApiModelProperty(value = "调整金额")
    private BigDecimal adjustAmount;
    @ApiModelProperty(value = "计提类型")
    private String accrualType;
    @ApiModelProperty(value = "是否删除 0：未删除1：已删除")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "计划日期")
    private Date planDate;

    @ApiModelProperty(value = "服务费总额")
    private BigDecimal serviceFeeTotal;
    //服务费总额
    private BigDecimal serviceFeeTotalNoTax;
    @ApiModelProperty(value = "服务费协议金额")
    private BigDecimal serviceFeeAgreedAmount;

    @ApiModelProperty(value = "实收服务费金额")
    private BigDecimal actualReceiveServiceFee;

    @ApiModelProperty(value = "合同一次性确认金额")
    private BigDecimal contractConfirmedAmount;

    @ApiModelProperty(value = "协议一次性确认金额")
    private BigDecimal agreedConfirmedAmount;

    @ApiModelProperty(value = "协议分摊金额")
    private BigDecimal agreedApportionAmount;

    @ApiModelProperty(value = "合同设备金额")
    private BigDecimal contractDeviceAmount;

    @ApiModelProperty(value = "计划分摊金额")
    private BigDecimal planApportionAmount;

    @ApiModelProperty(value = "期数")
    private Integer periods;
}
