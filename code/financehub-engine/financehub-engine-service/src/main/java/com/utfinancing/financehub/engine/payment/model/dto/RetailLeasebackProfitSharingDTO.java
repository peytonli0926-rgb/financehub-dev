package com.utfinancing.financehub.engine.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 零售融资租赁乘用车回租分润费确认统一接口。 */
@Data
@ApiModel("零售融资租赁回租分润费确认事件")
public class RetailLeasebackProfitSharingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空")
    @ApiModelProperty(value = "请求流水号，须全局唯一", required = true)
    private String orderId;

    @NotBlank(message = "事件编码[eventCode]不能为空")
    @ApiModelProperty(value = "起租日确认分润费/FRF_CONFIRM，或退回分润费/FRF_REFUND", required = true)
    private String eventCode;

    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "业务发生日期", required = true)
    private LocalDateTime businessDate;

    @NotBlank(message = "合同编号[contractCode]不能为空")
    @ApiModelProperty(value = "融资租赁合同编号", required = true)
    private String contractCode;

    @NotNull(message = "分润费金额[profitSharingAmount]不能为空")
    @DecimalMin(value = "0.01", message = "分润费金额[profitSharingAmount]必须大于0")
    @ApiModelProperty(value = "确认或退回的分润费金额", required = true)
    private BigDecimal profitSharingAmount;

    @ApiModelProperty(value = "关联起租单号")
    private String triggerOrderId;

    @ApiModelProperty(value = "币种，默认CNY")
    private String currency;

    @ApiModelProperty(value = "备注")
    private String remark;
}
