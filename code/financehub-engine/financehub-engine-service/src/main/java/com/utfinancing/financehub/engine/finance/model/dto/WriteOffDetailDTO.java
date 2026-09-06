package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WriteOffDetailDTO implements Serializable {

    //到账主体
    @ApiModelProperty(value = "到账主体", required=true)
    private String collectionAccountsBank;

    //到账主体
    @ApiModelProperty(value = "到账主体编号", required=true)
    private String collectionAccountsBankCode;

    @ApiModelProperty(value = "业务系统网银编号/批次号", required=true)
    private String ebankSerialNumber;

    // 网银到账金额
    @ApiModelProperty(value = "网银到账金额", required=true)
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "剩余未确认金额", required=true)
    private BigDecimal remainNonConfirmAmount;

    @ApiModelProperty(value = "冲销金额")
    private BigDecimal writeOffAmount;

    @ApiModelProperty(value = "认领主体")
    private String OrgId;

    @ApiModelProperty(value = "认领名称")
    private String OrgName;


    @ApiModelProperty(value = "冲销备注")
    private String remark;

    @ApiModelProperty(value = "合同号")
    private String contractCode;
}
