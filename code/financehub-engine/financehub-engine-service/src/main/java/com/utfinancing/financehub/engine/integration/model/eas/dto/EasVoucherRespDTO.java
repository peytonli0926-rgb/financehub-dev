package com.utfinancing.financehub.engine.integration.model.eas.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : lixin
 * @Date : Create in 31/10/2023
 */
@NoArgsConstructor
@Data
public class EasVoucherRespDTO {

    @ApiModelProperty("来源系统")
    private String system;

    @ApiModelProperty("凭证号")
    private String voucherNumber;

    @ApiModelProperty("凭证类型")
    private String voucherType;

    @ApiModelProperty("期间年")
    private String periodYear;

    @ApiModelProperty("期间月")
    private String periodMonth;

    @ApiModelProperty("响应标识")
    private String flag;

    @ApiModelProperty("返回日志")
    private String log;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("EAS生成凭证后的凭证编码")
    @JSONField(name = "eas_voucherNumber")
    @JsonProperty("eas_voucherNumber")
    private String easVoucherNumber;

    @ApiModelProperty("EAS对应凭证唯一码")
    @JSONField(name = "eas_voucherID")
    @JsonProperty("eas_voucherID")
    private String easVoucherId;

}
