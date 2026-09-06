package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-26
 * @Description : Charge Off汇总报表VO对象
 * @Modified :
 */
@Data
public class ChargeOffSummaryReportVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "核销状态")
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "核销时间")
    private LocalDateTime verificationDate;

    @ApiModelProperty(value = "财务核销敞口")
    private String financialExpenseAmount;

    @ApiModelProperty(value = "拨备转回金额")
    private String provisionReversalAmount;

    @ApiModelProperty(value = "拨备转回年份")
    private String provisionReversalYear;

    @ApiModelProperty(value = "税务核销日期")
    private LocalDateTime taxVerificationDate;

    @ApiModelProperty(value = "税务核销金额")
    private String taxVerificationAmount;

    @ApiModelProperty(value = "坏账核销余额")
    private String badDebtWriteOffBalance;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
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
