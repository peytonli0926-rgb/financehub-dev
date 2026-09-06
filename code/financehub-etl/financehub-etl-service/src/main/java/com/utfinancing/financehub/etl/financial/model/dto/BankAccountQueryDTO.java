package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :   BankAccount查询from对象
 * @Modified :
 */
@ApiModel("BankAccount查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BankAccountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "金蝶T_BD_ACCOUNTBANKS主键ID")
    private String easId;

    @ApiModelProperty(value = "账户编码")
    private String bankAccountCode;

    @ApiModelProperty(value = "账户名称")
    private String bankAccountName;

    @ApiModelProperty(value = "账号")
    private String bankAccountNumber;

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "币种")
    private String currencyCode;
}
