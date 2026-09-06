package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description : 服务费分摊表详情DTO对象
 * @Modified :
 */
@Data
public class ServiceFeeDetailsDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "业务日期")
    private Date businessDate;

    @ApiModelProperty(value = "服务费分摊表id")
    private Long serviceFeeId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "分摊方式(租赁收入分摊、服务费收入分摊)")
    private String allocationMethod;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "服务费实收（税后）")
    private BigDecimal serviceFeeReceived;

    @ApiModelProperty(value = "应分摊的服务费收入（税后）")
    private BigDecimal serviceFeeAllocationNoTax;

    @ApiModelProperty(value = "上月服务费应分摊金额（税后）")
    private BigDecimal lastMonthServiceFeeAllocationNoTax;

    @ApiModelProperty(value = "本月重分类调整(税后)")
    private BigDecimal reclassificationAdjustmentNoTaxAmount;

    @ApiModelProperty(value = "计提类型")
    private String accrualType;

    @ApiModelProperty(value = "本期以前")
    private BigDecimal beforeXYearMonthAmount;

    @ApiModelProperty(value = "本期调整")
    private BigDecimal xYearMonthAdjustmentAmount;

    @ApiModelProperty(value = "本期摊销后余额")
    private BigDecimal allocationAfterXYearMonthBalance;

    @ApiModelProperty(value = "实际未计提金额")
    private BigDecimal notAccruedAmount;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "本次分摊期数")
    private Integer periods;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "累计已计提金额")
    private BigDecimal accumulatedAccruedAmount;

    @ApiModelProperty(value = "分摊完结标记")
    private String allocationCompletionMark;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

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

}
