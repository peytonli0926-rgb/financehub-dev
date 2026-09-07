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

@Data
@ApiModel("零售融资租赁回租计提资产管理费事件")
public class RetailLeasebackManagementFeeAccrualDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空")
    @ApiModelProperty(value = "请求流水号，须全局唯一", required = true)
    private String orderId;

    @NotBlank(message = "事件编码[eventCode]不能为空")
    @ApiModelProperty(value = "计提资产管理费或JTZCGLF", required = true)
    private String eventCode;

    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "月末计提日期；由此确定会计期间", required = true)
    private LocalDateTime businessDate;

    @NotBlank(message = "合同编号[contractCode]不能为空")
    @ApiModelProperty(value = "融资租赁合同编号", required = true)
    private String contractCode;

    @NotNull(message = "管理费金额[managementFeeAmount]不能为空")
    @DecimalMin(value = "0.01", message = "管理费金额[managementFeeAmount]必须大于0")
    @ApiModelProperty(value = "本月应计提的不含税资产管理费；接口不传税额和税率", required = true)
    private BigDecimal managementFeeAmount;

    @ApiModelProperty(value = "币种，默认CNY")
    private String currency;

    @ApiModelProperty(value = "备注")
    private String remark;
}
