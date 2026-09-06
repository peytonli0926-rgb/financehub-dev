package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemBankMappingReconciliationVO {

    @ApiModelProperty("交易日期")
    @Excel(name = "交易日期")
    private String businessDate;

    @ApiModelProperty("勾稽编号")
    @Excel(name = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty("资金编号")
    @Excel(name = "资金编号")
    private String ebankNumber;

    @ApiModelProperty("大网银编号")
    @Excel(name = "大网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("到账金额")
    @Excel(name = "到账金额")
    private String matchAmount;

    @ApiModelProperty("批扣总金额")
    @Excel(name = "批扣总金额")
    private String collectAmount;

    @ApiModelProperty("差额")
    @Excel(name = "差额")
    private String diffAmount;

    @ApiModelProperty("是否发送到金蝶 0：否 1：是")
    @Excel(name = "是否发送到金蝶", readConverterExp = "0=否,1=是")
    private String sendKingdee;
}
