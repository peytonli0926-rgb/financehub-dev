package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ClaimConfirmVoucherDTO implements Serializable {

    @ApiModelProperty(value = "做账主体", required=true)
    private String createConfirmOrgId;

    @ApiModelProperty(value = "做账主体名称", required=true)
    private String createConfirmOrgName;

    @ApiModelProperty(value = "借贷方向:DR：借方；CR：贷方", required=true)
    private String crOrDt;

    @ApiModelProperty(value = "科目编码", required=true)
    private String accountNumber;

    @ApiModelProperty(value = "科目名称", required=true)
    private String accountName;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherComments;

    @ApiModelProperty(value = "金额", required=true)
    private BigDecimal amount;
//
//    @ApiModelProperty(value = "客户/凭证维度信息", required=true)
//    private List<String> assistFlags;

    @ApiModelProperty(value = "客户编码", required=true)
    private String clientCode;

    @ApiModelProperty(value = "客户名称", required=true)
    private String clientName;

    @ApiModelProperty(value = "合同编码", required=true)
    private String contractCode;

    @ApiModelProperty(value = "银行账号", required=true)
    private String bankNo;

    @ApiModelProperty(value = "借款合同编号", required=true)
    private String loansContractCode;
}
