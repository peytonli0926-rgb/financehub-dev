package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : 应交增值税VO对象
 * @Modified :
 */
@Data
public class PayVatExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "合同类型")
    private String contractType;

    @Excel(name = "租赁类型")
    private String leaseType;

    @Excel(name = "税率", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxRate;

    @Excel(name = "客户编号")
    private String clientCode;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "开票标识")
    private String invoicingFlag;

    @Excel(name = "合同状态")
    private String contractStatus;

    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @Excel(name = "开票主体")
    private String orgName;

    @Excel(name = "应开税额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxPayable;

    @Excel(name = "已计提税额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxAccrued;

    @Excel(name = "已开票税额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxInvoiced;

    @Excel(name = "实际剩余税额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualTaxBalance;

    @Excel(name = "应剩余税额（按计划）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal shouldTaxBalance;

    @Excel(name = "科目余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accountBalance;

    @Excel(name = "报表余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal reportBalance;

    @Excel(name = "异常类型")
    private String exceptionType;

    @Excel(name = "租赁合同编号")
    private String contractCodeM;

    @Excel(name = "服务费实收金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal serviceFeeReceived;

    @Excel(name = "服务费实收日期", dateFormat = "yyyy-MM-dd")
    private Date serviceFeeReceivedDate;

    @Excel(name = "留购价余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal retainedPriceBalance;

    @Excel(name = "合同起租日", dateFormat = "yyyy-MM-dd")
    private LocalDateTime leaseDateStart;

    @Excel(name = "合同到期日", dateFormat = "yyyy-MM-dd")
    private LocalDateTime leaseDateEnd;

    @Excel(name = "处理状态",readConverterExp="0=未录入,1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;


}
