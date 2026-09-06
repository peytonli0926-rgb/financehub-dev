package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ModifyIncomeDateTemplateDownloadExcel implements Serializable {

    @Excel(name = "业务系统网银编号/批次号", comment = "和[业务系统批扣流水号]列至少输入一个")
    private String businessEbankNo;

    @Excel(name = "业务系统批扣流水号", comment = "和[业务系统网银编号/批次号]列至少输入一个")
    private String ebankSerialNumber;

    @Excel(name = "原入账月份", comment = "必填(yyyy-MM)", cellType = Excel.ColumnType.DATE, dateFormat = "yyyy-MM")
    private Date incomeDateOld;

    @Excel(name = "调整入账日期", comment = "必填(yyyy-MM-dd)", cellType = Excel.ColumnType.DATE, dateFormat = "yyyy-MM-dd")
    private Date incomeDateAdjust;
}
