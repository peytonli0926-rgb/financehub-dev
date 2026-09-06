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
 * @Date : Create in 2023-12-05
 * @Description :   FundSystemBalance查询from对象
 * @Modified :
 */
@ApiModel("FundSystemBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class FundSystemBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "银行账号")
    private String bankNo;

    @ApiModelProperty(value = "银行账户余额")
    private String bankAccountBalance;

    @ApiModelProperty(value = "银行账户发生额")
    private String bankAccountAmount;

    @ApiModelProperty(value = "交易类型（收款：collection，付款：payment）")
    private String transactionType;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "交易流水号")
    private String orderId;
}
