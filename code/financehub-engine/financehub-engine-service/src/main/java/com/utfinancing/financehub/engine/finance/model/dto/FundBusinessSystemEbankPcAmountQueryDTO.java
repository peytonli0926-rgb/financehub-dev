package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description :   FundBusinessSystemEbankPcAmount查询from对象
 * @Modified :
 */
@ApiModel("FundBusinessSystemEbankPcAmount查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class FundBusinessSystemEbankPcAmountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "ebank_amount_id")
    private Long ebankAmountId;

    @ApiModelProperty(value = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty(value = "业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "批次金额")
    private String businessMatchAmount;
}
