package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TailDifferenceAdjustmentDetailExcelVO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private Date accountDate;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码",width = 20)
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称",width = 20)
    private String accountName;

    @ApiModelProperty(value = "科目余额")
    @Excel(name = "科目余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "应收租金余额")
    @Excel(name = "应收租金余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值余额")
    @Excel(name = "应收期末残值余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "未实现融资收益-待摊收益")
    @Excel(name = "未实现融资收益-待摊收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeAfterTotal;

    @ApiModelProperty(value = "未实现融资租赁收益-待摊收益")
    @Excel(name = "未实现融资租赁收益-待摊收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeAfterLeaseTotal;

    @ApiModelProperty(value = "应付设备款余额")
    @Excel(name = "应付设备款余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableDevice;

    @ApiModelProperty(value = "应付其他款项余额")
    @Excel(name = "应付其他款项余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableOther;

    @ApiModelProperty(value = "合同状态")
    @Excel(name = "合同状态",width = 20)
    private String contractStatus;

    @ApiModelProperty(value = "约定到期日")
    @Excel(name = "约定到期日",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxRate;

    @ApiModelProperty(value = "应收总额")
    @Excel(name = "应收总额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receiveSum;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;
}
