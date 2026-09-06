package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : 应付保险费导入
 * @Modified :
 */
@Data
public class PayableInsuranceDetailImport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "业务日期", cellType = Excel.ColumnType.DATE, type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;

    @Excel(name = "记账日期", cellType = Excel.ColumnType.DATE, type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accountDate;

    @Excel(name = "签约主体", type = Excel.Type.IMPORT)
    private String orgId;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "客户名称", type = Excel.Type.IMPORT)
    private String clientName;

    @Excel(name = "结转金额", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.IMPORT)
    private BigDecimal carryoverAmount;

//    @Excel(name = "保险费支付报表余额", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.IMPORT)
//    private BigDecimal payableInsuranceBalanceReport;


}
