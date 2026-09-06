package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UploadNonLeaseResultTemplateDTO  implements Serializable {

    @Excel(name = "对账月份", comment = "必填(yyyy-MM)", cellType = Excel.ColumnType.DATE, dateFormat = "yyyy-MM")
    private String accountCheckingMonth;

    @Excel(name = "到账主体")
    private String collectionAccountsBank;

    @Excel(name = "业务系统网银编号-小网银", comment = "必填")
    private String ebankSerialNumber;

    @Excel(name = "非租未认领金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal nonLeaseNonClaimAmount;

    @Excel(name = "非租未认领原因")
    private String nonLeaseNonClaimReasons;
}
