package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@ApiModel
public class ConvertTransferOtherVO {
    private Long id;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("客户名称")
    private String clientName;

    @ApiModelProperty("财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty("转让方")
    private String transferParty;

    @ApiModelProperty("受让方")
    private String transfereeParty;

    @ApiModelProperty("转让时间")
    private LocalDate accountDate;

    @ApiModelProperty("转让基准日")
    private LocalDate referenceDate;

    @ApiModelProperty("转让后是否开发票 0=否,1=是")
    private String invoiceFlag;

    @ApiModelProperty("其他收入")
    private BigDecimal otherIncome;

    @ApiModelProperty("其他成本")
    private BigDecimal otherOutcome;

    @ApiModelProperty("处理状态,1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    public ConvertTransferOtherVO(ConvertTransferOtherEntity entity) {
        this.id = entity.getId();
        this.contractCode = entity.getContractCode();
        this.clientName = entity.getClientName();
        this.financialContractStatus = entity.getFinancialContractStatus();
        this.transferParty = entity.getTransferParty();
        this.transfereeParty = entity.getTransfereeParty();
        this.accountDate = Optional.ofNullable(entity.getAccountDate()).map(LocalDateTime::toLocalDate).orElse(null);
        this.referenceDate = Optional.ofNullable(entity.getReferenceDate()).map(LocalDateTime::toLocalDate).orElse(null);
        this.invoiceFlag = entity.getInvoiceFlag();
        this.otherIncome = entity.getOtherIncome();
        this.otherOutcome = entity.getOtherOutcome();
        this.processStatus = entity.getProcessStatus();

    }
}
