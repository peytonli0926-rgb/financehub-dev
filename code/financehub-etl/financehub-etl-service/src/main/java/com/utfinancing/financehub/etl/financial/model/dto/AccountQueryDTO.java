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
 * @Date : Create in 2023-11-16
 * @Description :   Account查询from对象
 * @Modified :
 */
@ApiModel("Account查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "金额类型")
    private String fundType;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目性质")
    private String accountCategory;

    @ApiModelProperty(value = "余额方向（DR/CR）")
    private String debitCreditType;

    @ApiModelProperty(value = "凭证维度-客户(0:否 1是)")
    private String clientFlag;

    @ApiModelProperty(value = "凭证维度-合同(0:否 1是)")
    private String contractFlag;
}
