package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-30
 * @Description :   CheckCommonData查询from对象
 * @Modified :
 */
@ApiModel("CheckCommonData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckCommonDataQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "与中台sql关联字段组合成的json字段")
    private String joinField;

    @ApiModelProperty(value = "与中台sql查询的比较字段组合的json字段")
    private String queryField;

    @ApiModelProperty(value = "对接系统db")
    private String dbCode;
}
