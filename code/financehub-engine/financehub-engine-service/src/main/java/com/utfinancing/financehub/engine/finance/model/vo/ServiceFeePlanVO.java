package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description : 服务费计划表VO对象
 * @Modified :
 */
@Data
public class ServiceFeePlanVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "分摊方式(租赁收入分摊、服务费收入分摊)")
    private String allocationMethod;
    @ApiModelProperty(value = "业务code")
    private String businessCode;
    @ApiModelProperty(value = "业务名称")
    private String businessName;
    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;
    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    private String orgName;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;
    private String serviceOrgName;

    @ApiModelProperty(value = "服务费摊销利率")
    private BigDecimal serviceFeeAmortizationRate;

    @ApiModelProperty(value = "实际计提金额")
    private BigDecimal actualAccruedAmount;

    @ApiModelProperty(value = "调整金额")
    private BigDecimal adjustAmount;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;

    @ApiModelProperty(value = "服务费总额")
    private BigDecimal serviceFeeTotal;
    //服务费总额
    private BigDecimal serviceFeeTotalNoTax;
    @ApiModelProperty(value = "服务费协议金额")
    private BigDecimal serviceFeeAgreedAmount;

    @ApiModelProperty(value = "实收服务费金额")
    private BigDecimal actualReceiveServiceFee;
    private BigDecimal actualReceiveServiceFeeNoTax;

    @ApiModelProperty(value = "合同一次性确认金额")
    private BigDecimal contractConfirmedAmount;

    @ApiModelProperty(value = "协议一次性确认金额")
    private BigDecimal agreedConfirmedAmount;

    @ApiModelProperty(value = "协议分摊金额")
    private BigDecimal agreedApportionAmount;
    private BigDecimal agreedApportionAmountNoTax;

    @ApiModelProperty(value = "合同设备金额")
    private BigDecimal contractDeviceAmount;

    @ApiModelProperty(value = "计划分摊金额")
    private BigDecimal planApportionAmount;

    @ApiModelProperty(value = "期数")
    private Integer periods;
    @ApiModelProperty(value = "计提类型")
    private String accrualType;
    //应分摊的服务费收入（税后）
    private BigDecimal planApportionNoTax;

    //本期计划计提金额（税前）
    private BigDecimal planAmount;
    //本期计划计提金额（税后）
    private BigDecimal planAmountNoTax;
    //服务费实收（税后）
    private BigDecimal actualReceiveNoTax;

    @ApiModelProperty(value = "凭证状态")
    private String voucherStatus;
    @ApiModelProperty(value = "是否分摊标记")
    private Boolean sharingServiceFeeFlag;
    private String sharingServiceFeeFlagStr;

    @ApiModelProperty(value = "分摊完结标记")
    private Boolean endSharingServiceFeeFlag;
    private String endSharingServiceFeeFlagStr;

    @ApiModelProperty(value = "是否特殊状态调整")
    private Boolean specialStatusAdjustmentFlag;
    private String specialStatusAdjustmentFlagStr;
    @ApiModelProperty(value = "本月调整（税后）")
    private BigDecimal thisMonthAdjustmentAmountNoTax;
}
