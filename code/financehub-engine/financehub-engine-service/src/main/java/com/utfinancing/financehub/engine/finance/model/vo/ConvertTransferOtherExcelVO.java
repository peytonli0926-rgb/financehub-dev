package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConvertTransferOtherExcelVO {

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @Excel(name = "转让方")
    private String transferParty;

    @Excel(name = "受让方")
    private String transfereeParty;

    @Excel(name = "转让时间", dateFormat = "yyyy-MM-dd")
    private LocalDateTime accountDate;

    @Excel(name = "转让基准日", dateFormat = "yyyy-MM-dd")
    private LocalDateTime referenceDate;

    @Excel(name = "转让后是否开发票", readConverterExp = "0=否,1=是")
    private String invoiceFlag;

    @Excel(name = "其他收入")
    private BigDecimal otherIncome;

    @Excel(name = "其他成本")
    private BigDecimal otherOutcome;

    @Excel(name = "处理状态", width = 20, readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶")
    private String processStatus;

    public ConvertTransferOtherExcelVO(ConvertTransferOtherEntity entity) {
        this.contractCode = entity.getContractCode();
        this.clientName = entity.getClientName();
        this.financialContractStatus = entity.getFinancialContractStatus();
        this.transferParty = entity.getTransferParty();
        this.transfereeParty = entity.getTransfereeParty();
        this.accountDate = entity.getAccountDate();
        this.referenceDate = entity.getReferenceDate();
        this.invoiceFlag = entity.getInvoiceFlag();
        this.otherIncome = entity.getOtherIncome();
        this.otherOutcome = entity.getOtherOutcome();
        this.processStatus = entity.getProcessStatus();

    }
}
