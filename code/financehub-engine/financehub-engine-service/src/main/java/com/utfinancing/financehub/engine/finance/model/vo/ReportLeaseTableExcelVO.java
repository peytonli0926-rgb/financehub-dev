package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description : 报表模块-租赁大表VO对象
 * @Modified :
 */
@Data
public class ReportLeaseTableExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "签约主体Id")
//    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

//    @ApiModelProperty(value = "客户编码")
//    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "系统代码")
    private String systemCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "会计起租日")
    private String leaseDateStart;

    @ApiModelProperty(value = "合同约定到期日")
    private String leaseDateEnd;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivableDownpaymentBalance;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收手续费")
    private BigDecimal receivableCommissionBalance;

    @ApiModelProperty(value = "应收保险费")
    private BigDecimal receivableInsuranceBalance;

    @ApiModelProperty(value = "应收其他收入")
    private BigDecimal receivableOtherincomeBalance;

    @ApiModelProperty(value = "应收返利")
    private BigDecimal receivableRebateBalance;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "应收销项税-本金")
    private BigDecimal receivableOutputtaxBaseBalance;

    @ApiModelProperty(value = "未实现收益-总")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "未实现收益-不含服务费")
    private BigDecimal rentalIncomeAfterTotal;

    @ApiModelProperty(value = "融资租赁收益余额")
    private BigDecimal leaseRevenueBalance;

    @ApiModelProperty(value = "减值准备余额")
    private BigDecimal depreciationReservesBalance;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "TA重分类")
    private BigDecimal taAmount;

    @ApiModelProperty(value = "逾期收益")
    private BigDecimal overdueEarnings;

//    @ApiModelProperty(value = "合同表Id，用于前端跳转合同详情")
//    private Long contractId;
//
//    @ApiModelProperty(value = "凭证Id，逗号分类")
//    private String voucherId;

    @ApiModelProperty(value = "应收融资租赁款总额")
    private BigDecimal receivableTotalBalance;

    @ApiModelProperty(value = "日期")
    private String queryDate;

    @ApiModelProperty(value = "应收融资租赁款净值")
    private BigDecimal receivableNetBalance;

    @ApiModelProperty(value = "未实现收益-咨询服务费分摊")
    private BigDecimal receivableServiceFeeBalance;
}
