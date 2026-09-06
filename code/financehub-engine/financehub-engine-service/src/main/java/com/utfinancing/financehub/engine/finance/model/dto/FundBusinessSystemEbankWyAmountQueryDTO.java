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
 * @Description :   FundBusinessSystemEbankWyAmount查询from对象
 * @Modified :
 */
@ApiModel("FundBusinessSystemEbankWyAmount查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class FundBusinessSystemEbankWyAmountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "ebank_amount_id")
    private Long ebankAmountId;

    @ApiModelProperty(value = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty(value = "资金系统网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "网银金额")
    private String wyAmount;
}
