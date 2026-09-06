package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BatchClaimConfirmTemplateDownloadInputDTO implements Serializable {

    @ApiModelProperty(value = "资金系统网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "网银余额非零, 1:非零 0:零")
    private String notZero;
}
