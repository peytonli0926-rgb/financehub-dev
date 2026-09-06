package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : 银行账户DTO对象
 * @Modified :
 */
@Data
public class BankAccountDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
