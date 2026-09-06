package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2024-07-02
 * @Description :   NonConfirmAmountForBusiness查询from对象
 * @Modified :
 */
@ApiModel("NonConfirmAmountForBusiness查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class NonConfirmAmountForBusinessQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对账月份")
    private String accountCheckingMonth;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "系统金额")
    private String systemAmount;
}
