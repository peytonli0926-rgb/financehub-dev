package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferContractFeeVO {

    @ApiModelProperty
    private Long id;

    @ApiModelProperty("业务日期")
    private LocalDate uploadDate;

    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    @ApiModelProperty("签约主体")
    private String orgId;

    @ApiModelProperty("金额")
    private BigDecimal transferFeeAmount;

    @ApiModelProperty("处理状态")
    private String processStatus;

    private String voucherId;


    public ConvertTransferContractFeeVO(ConvertTransferContractFeeEntity entity) {
        this.id = entity.getId();
        this.uploadDate = entity.getUploadDate();
        this.accountDate = entity.getAccountDate();
        this.orgId = entity.getOrgId();
        this.transferFeeAmount = entity.getTransferFeeAmount();
        this.processStatus = entity.getProcessStatus();
        this.voucherId = entity.getVoucherId();
    }
}
