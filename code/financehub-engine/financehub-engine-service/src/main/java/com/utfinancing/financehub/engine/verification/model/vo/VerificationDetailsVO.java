package com.utfinancing.financehub.engine.verification.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-12
 * @Description : VO对象
 * @Modified :
 */
@Data
public class VerificationDetailsVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "核销表id")
    private Long verificationId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivableDownpayment;

    @ApiModelProperty(value = "应收手续费")
    private BigDecimal receivableCommission;

    @ApiModelProperty(value = "应收保险费")
    private BigDecimal receivableInsurance;

    @ApiModelProperty(value = "应收其他收入")
    private BigDecimal receivableOtherincome;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "应付设备款-暂估")
    private BigDecimal payableDeviceEstimate;

    @ApiModelProperty(value = "应付经销商服务费-暂估")
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估")
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估")
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估")
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估")
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估")
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "财务核销敞口")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "补偿提备")
    private BigDecimal compensationProvisionAmount;

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

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "凭证id")
    private String voucherId;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    @ApiModelProperty("凭证报错信息")
    private String errorInfo;

    @ApiModelProperty("创建人姓名")
    private String createName;


}
