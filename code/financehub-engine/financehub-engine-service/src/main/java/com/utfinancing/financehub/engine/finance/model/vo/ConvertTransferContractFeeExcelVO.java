package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvertTransferContractFeeExcelVO {

    @Excel(name = "业务日期")
    private LocalDate uploadDate;

    @Excel(name = "记账日期")
    private LocalDate accountDate;

    @Excel(name = "签约主体")
    private String orgId;

    @Excel(name = "金额")
    private BigDecimal transferFeeAmount;

    @Excel(name = "处理状态")
    private String processStatus;


    public ConvertTransferContractFeeExcelVO(ConvertTransferContractFeeEntity entity) {
        this.uploadDate = entity.getUploadDate();
        this.accountDate = entity.getAccountDate();
        this.orgId = entity.getOrgId();
        this.transferFeeAmount = entity.getTransferFeeAmount();
        this.processStatus = entity.getProcessStatus();
    }
}
