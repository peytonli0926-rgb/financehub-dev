package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :   Business查询from对象
 * @Modified :
 */
@ApiModel("Business查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "科目余额表编码")
    private String accountBalanceCode;
}
