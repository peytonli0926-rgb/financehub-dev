package com.utfinancing.financehub.engine.finance.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@ApiModel("汇总页导出VO")
@Data
public class AssetAbsRedeemExportExcelVO {

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "出表期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "赎回起算日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "赎回起算日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "实际赎回日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际赎回日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    @ApiModelProperty(value = "赎回价格")
    @Excel(name = "赎回价格",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal redeemPriceTotal;

}
