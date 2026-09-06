package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description : 服务费分摊表详情VO对象
 * @Modified :
 */
@Data
public class ServiceFeeDetailsNewExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "服务费协议编号")
    private String serviceFeeNo;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "合同主体")
    private String orgId;

    @Excel(name = "服务费签约主体")
    private String serviceOrgId;

    @Excel(name = "分摊方式", readConverterExp = "0=租赁收入分摊,1=服务费收入分摊")
    private String allocationMethod;

    @Excel(name = "业务类型")
    private String businessName;

    @Excel(name = "会计起租日", cellType = Excel.ColumnType.DATE)
    private Date leaseDateStart;

    @Excel(name = "合同约定到期日", cellType = Excel.ColumnType.DATE)
    private Date leaseDateEnd;

    @Excel(name = "服务费实收（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivedServiceFeeTaxInclude;

    @Excel(name = "服务费实收（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivedServiceFeeNoTax;

    @Excel(name = "应分摊的服务费收入（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal shouldApportionmentAmountTaxInclude;

    @Excel(name = "应分摊的服务费收入（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal shouldApportionmentAmountNoTax;

    @Excel(name = "上月服务费应分摊金额（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lastMonthShouldApportionmentAmountNoTax;

    @Excel(name = "本月重分类调整(税后)", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthReclassificationAdjustmentAmountNoTax;

    @Excel(name = "上月服务费应分摊金额（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lastMonthShouldApportionmentAmountTaxInclude;

    @Excel(name = "本月重分类调整(税前)", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthReclassificationAdjustmentAmountTaxInclude;

    @Excel(name = "计提类型", readConverterExp = "0=新增,1=调整")
    private String accrualType;

    @Excel(name = "本月以前（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal beforeAccruedAmount;

    @Excel(name = "本月调整（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accruedAmount;

    @Excel(name = "本月摊销后余额（税后）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal afterAccruedAmount;

    @Excel(name = "合同状态")
    private String contractStatus;

    @Excel(name = "特殊合同状态")
    private String financialContractStatus;

    @Excel(name = "本期之前（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal beforeCurrentPeriodPlanAmountTaxInclude;

    @Excel(name = "本期发生（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal currentPeriodPlanAmountTaxInclude;

    @Excel(name = "本期之后（税前）", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal afterCurrentPeriodPlanAmountTaxInclude;

    @Excel(name = "计提凭证状态")
    private String voucherStatus;

    @Excel(name = "分摊完结标记", readConverterExp = "0=否,1=是")
    private String endSharingServiceFeeFlag;

    @Excel(name = "异常类型")
    private String exceptionType;

    @Excel(name = "是否分摊标记", readConverterExp = "0=否,1=是")
    private String sharingServiceFeeFlag;

    @Excel(name = "是否特殊状态调整", readConverterExp = "0=否,1=是")
    private String specialStatusAdjustmentFlag;

}
