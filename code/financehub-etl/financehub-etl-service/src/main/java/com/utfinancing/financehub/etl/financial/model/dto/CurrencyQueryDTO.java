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
 * @Description :   Currency查询from对象
 * @Modified :
 */
@ApiModel("Currency查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "编码")
    private String currencyCode;

    @ApiModelProperty(value = "名称")
    private String currenctName;

    @ApiModelProperty(value = "金蝶主键ID")
    private String easId;

    @ApiModelProperty(value = "金蝶编码")
    private String easCode;

    @ApiModelProperty(value = "金蝶编码状态：普通=1,作废=2")
    private String easStatus;
}
