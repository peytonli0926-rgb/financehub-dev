package com.utfinancing.financehub.engine.finance.model.dto;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.FinancialContractStatusEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ConvertTransferOtherDTO {

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "财务合同状态")
    private String contractStatus;

    @Excel(name = "转让方")
    private String transferParty;

    @Excel(name = "受让方")
    private String transfereeParty;

    @Excel(name = "转让时间")
    private Date accountDate;

    @Excel(name = "转让基准日")
    private Date referenceDate;

    @Excel(name = "转让后是否开发票", readConverterExp = "0=否,1=是")
    private String invoiceFlag;

    @Excel(name = "其他收入")
    private BigDecimal otherIncome;

    @Excel(name = "其他成本")
    private BigDecimal otherOutcome;

    public ConvertTransferOtherEntity toEntity() {
        ConvertTransferOtherEntity entity = new ConvertTransferOtherEntity();
        entity.setContractCode(contractCode);
        entity.setFinancialContractStatus(contractStatus);
        entity.setTransferParty(transferParty);
        entity.setTransfereeParty(transfereeParty);
        entity.setAccountDate(DateUtil.toLocalDateTime(accountDate));
        entity.setReferenceDate(DateUtil.toLocalDateTime(referenceDate));
        entity.setInvoiceFlag(invoiceFlag);
        entity.setOtherIncome(otherIncome);
        entity.setOtherOutcome(otherOutcome);
        entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        return entity;
    }
}
