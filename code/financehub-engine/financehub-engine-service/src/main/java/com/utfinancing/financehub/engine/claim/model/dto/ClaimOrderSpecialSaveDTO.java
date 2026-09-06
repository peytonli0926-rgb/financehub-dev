package com.utfinancing.financehub.engine.claim.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description : 报销系统-报销单专项费明细DTO对象
 * @Modified :
 */
@Data
public class ClaimOrderSpecialSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "序号")
    private String no;

    @ApiModelProperty(value = "组织")
    private String org;

    @ApiModelProperty(value = "系统")
    private String sourceSystem;

    @ApiModelProperty(value = "合同号")
    private String contractNum;

    @ApiModelProperty(value = "承租人")
    private String tenant;

    @ApiModelProperty(value = "账期")
    private String accountDate;

    @ApiModelProperty(value = "单价")
    private String unitPrice;

    @ApiModelProperty(value = "设备款不含税额")
    private String equipNotaxAmount;

    @ApiModelProperty(value = "设备款进项税")
    private String equipInputTax;

    @ApiModelProperty(value = "安装费")
    private String installAmount;

    @ApiModelProperty(value = "安装费不含税额")
    private String installNotaxAmount;

    @ApiModelProperty(value = "安装费进项税")
    private String installInputTax;

    @ApiModelProperty(value = "服务费")
    private String serviceAmount;

    @ApiModelProperty(value = "服务费不含税额")
    private String serviceNotaxAmount;

    @ApiModelProperty(value = "服务费进项税")
    private String serviceInputTax;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "是否转天津")
    private String tianjiFlag;

    @ApiModelProperty(value = "付款金额")
    private String paymentAmount;

    @ApiModelProperty(value = "不含税金额")
    private String paymentNotaxAmount;

    @ApiModelProperty(value = "进项税")
    private String paymentInputTax;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "凭证标识")
    private String voucherFlag;

    @ApiModelProperty(value = "item01")
    private String item01;

    @ApiModelProperty(value = "item01")
    private String item02;

    @ApiModelProperty(value = "item01")
    private String item03;

    @ApiModelProperty(value = "item01")
    private String item04;

    @ApiModelProperty(value = "item01")
    private String item05;


}
