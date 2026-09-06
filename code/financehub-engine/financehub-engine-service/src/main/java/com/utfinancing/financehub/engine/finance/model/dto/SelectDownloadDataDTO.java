package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectDownloadDataDTO implements Serializable {
    @Excel(name = "到账主体",width = 20)
    private String collectionAccountsBank;

    @Excel(name = "认领主体",width = 20)
    private String orgName;

    @Excel(name = "业务系统网银编号/批次号",width = 20)
    private String businessEbankNumber;

    @Excel(name = "业务系统批扣流水号",width = 20)
    private String ebankSerialNumber;

    @Excel(name = "业务系统",width = 15)
    private String systemCode;

    @Excel(name = "最后变化日期",width = 10, dateFormat = "yyyy-MM-dd")
    private String lastChangeDate;

    @Excel(name = "网银到账日期",width = 10, dateFormat = "yyyy-MM-dd")
    private Date ebankIncomeDate;

    @Excel(name = "币种",width = 10)
    private String currencyType;

    @Excel(name = "网银到账金额",width = 10)
    private BigDecimal bankAmount;

    @Excel(name = "最新剩余未确认金额",width = 10)
    private BigDecimal remainAmount;

    @Excel(name = "已确认金额",width = 10)
    private BigDecimal claimedAmount;

    @Excel(name = "本次确认金额",width = 10)
    private BigDecimal claimAmount;

    @Excel(name = "网银确认日期",width = 10, dateFormat = "yyyy-MM-dd")
    private Date ebankConfirmDate;

    private String contractCode;

    private String operationType;

    @Excel(name = "网银确认内容",width = 30)
    private String ebankConfirmedContent;

    @Excel(name = "网银是否业务相关",width = 10)
    private String isRelationBusiness;

    @Excel(name = "到账银行账号",width = 10)
    private String collectionAccountsBankNo;

    @Excel(name = "到帐银行账号名称",width = 10)
    private String collectionAccountsBankName;

    private String clientCode;

    @Excel(name = "付款客户",width = 10)
    private String clientName;

    @Excel(name = "银行交易摘要",width = 10)
    private String bankSummary;

    @Excel(name = "银行交易备注",width = 10)
    private String comment;

    @Excel(name = "付款方账户",width = 10)
    private String clientAccountsBankNo;

    @Excel(name = "历史网银编号调整记录",width = 40)
    private String adjustmentRecord;

    @Excel(name = "备注",width = 20)
    private String remark;

    @Excel(name = "异常类型",width = 40)
    private String exceptionType;

}
