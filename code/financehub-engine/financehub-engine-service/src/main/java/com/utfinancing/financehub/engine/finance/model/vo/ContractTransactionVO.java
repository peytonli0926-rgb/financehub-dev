package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractTransactionVO {

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate voucherDate;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "会计日期")
    private Integer periodCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "回笼租金")
    private BigDecimal buybackRentAmount;

    @ApiModelProperty(value = "回笼本金")
    private BigDecimal buybackLossesAmount;

    @ApiModelProperty(value = "回笼利息")
    private BigDecimal buybackInterestAmount;

    @ApiModelProperty(value = "回笼期数")
    private String periods;

    @ApiModelProperty(value = "回收罚息")
    private BigDecimal recoveryPenaltyInterestAmount;

    @ApiModelProperty(value = "收取咨询服务费")
    private BigDecimal consultancyServiceAmount;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRentAmount;

    @ApiModelProperty(value = "应收本息")
    private BigDecimal receivablePriInterestAmount;

    @ApiModelProperty(value = "应收利息")
    private BigDecimal receivableInterestAmount;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivablePayment;

    @ApiModelProperty(value = "id")
    private Long interfaceId;


}
