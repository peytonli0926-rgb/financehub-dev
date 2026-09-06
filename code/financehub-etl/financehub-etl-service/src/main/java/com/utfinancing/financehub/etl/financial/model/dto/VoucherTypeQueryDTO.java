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
 * @Description :   VoucherType查询from对象
 * @Modified :
 */
@ApiModel("VoucherType查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherTypeQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "凭证类型编码")
    private String typeCode;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "金蝶T_BD_VOUCHERTYPES主键ID")
    private String easId;
}
