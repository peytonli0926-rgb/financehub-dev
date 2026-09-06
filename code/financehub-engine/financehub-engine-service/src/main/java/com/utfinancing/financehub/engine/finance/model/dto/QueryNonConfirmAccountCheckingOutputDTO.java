package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QueryNonConfirmAccountCheckingOutputDTO implements Serializable {

    //对账月份
    @ApiModelProperty(value = "对账月份")
    private String accountCheckingMonth;

    //到账主体
    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    //系统编码
    @ApiModelProperty(value = "业务系统")
    private String systemCode;

    //业务系统的网银编号
    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String businessEbankNumber;

    //业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankSerialNumber;

    //入账日期
    @ApiModelProperty(value = "入账日期")
    private LocalDateTime businessHappenDate;

    //账龄分类
    @ApiModelProperty(value = "账龄分类")
    private String accountAgeClass;

    //月初余额
    @ApiModelProperty(value = "本月初余额")
    private String monthInitBalance;

    //本月贷方发生额
    @ApiModelProperty(value = "本月贷方发生额")
    private String curMonthCreditAmount;

    //本月余额
    @ApiModelProperty(value = "本月余额")
    private String curMonthBalance;

    //系统金额
    @ApiModelProperty(value = "系统金额")
    private String systemAmount;

    //差额
    @ApiModelProperty(value = "差额")
    private String diffAmount;

    //财务对账备注
    @ApiModelProperty(value = "财务对账备注")
    private String accountCheckingComments;

    //财务初分类
    @ApiModelProperty(value = "财务初分类")
    private String financialPrimaryClassic;

    //运营部确认款项性质
    @ApiModelProperty(value = "运营部确认款项性质")
    private String confirmAccountProperty;

    // 到账主体编码
    @ApiModelProperty(value = "到账主体编码")
    private String collectionAccountsBankCode;
}
