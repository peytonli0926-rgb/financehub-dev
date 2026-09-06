package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BatchModifyTemplateDTO implements Serializable {

    @Excel(name = "对账月份", comment = "必填(yyyy-MM)", cellType = Excel.ColumnType.DATE, dateFormat = "yyyy-MM")
    private String accountCheckingMonth;

    @Excel(name = "到账主体")
    private String collectionAccountsBank;

    @Excel(name = "业务系统")
    private String systemCode;

    @Excel(name = "业务系统的网银编号-小网银", comment = "必填")
    private String ebankSerialNumber;

    @Excel(name = "comments")
    private String remark;

    @Excel(name = "财务部初分类")
    private String financialPrimaryClassic;

    @Excel(name = "运营部确认款项性质")
    private String confirmAccountProperty;

    @Excel(name = "非租对账备注")
    private String nonLeaseAccountCheckingComments;
    @Excel(name = "运营部历史备注")
    private String operateHistoryComments;
}
