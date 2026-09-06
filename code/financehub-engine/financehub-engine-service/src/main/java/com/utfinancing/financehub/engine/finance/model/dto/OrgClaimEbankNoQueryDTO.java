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
 * @Date : Create in 2025-06-06
 * @Description :   OrgClaimEbankNo查询from对象
 * @Modified :
 */
@ApiModel("OrgClaimEbankNo查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgClaimEbankNoQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "网银编号")
    private String ebankNo;
}
