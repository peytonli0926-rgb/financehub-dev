package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2025-05-15
 * @Description :   OutstandingAmountInit查询from对象
 * @Modified :
 */
@ApiModel("OutstandingAmountInit查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OutstandingAmountInitQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "科目编号")
    private String accountNumber;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "金额")
    private String endBalanceFor;
}
