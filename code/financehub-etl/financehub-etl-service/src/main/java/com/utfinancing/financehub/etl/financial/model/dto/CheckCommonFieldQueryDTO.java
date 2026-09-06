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
 * @Description :   CheckCommonField查询from对象
 * @Modified :
 */
@ApiModel("CheckCommonField查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckCommonFieldQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对接系统db")
    private String dbCode;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "查询字段")
    private String commonField;

    @ApiModelProperty(value = "字段类型 1 连接字段， 2 查询字段")
    private String fieldType;

    @ApiModelProperty(value = "sql里查询的字段顺序")
    private Integer fieldOrder;
}
