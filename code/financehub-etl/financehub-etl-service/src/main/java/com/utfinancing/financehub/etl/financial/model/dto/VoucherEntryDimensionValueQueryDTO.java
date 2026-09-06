package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-19
 * @Description :   VoucherEntryDimensionValue查询from对象
 * @Modified :
 */
@ApiModel("VoucherEntryDimensionValue查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherEntryDimensionValueQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "维度:用|分隔")
    private String dim;

    @ApiModelProperty(value = "维度值:用|分隔")
    private String dimValue;

    @ApiModelProperty(value = "来源")
    private String sourceSystem;
}
