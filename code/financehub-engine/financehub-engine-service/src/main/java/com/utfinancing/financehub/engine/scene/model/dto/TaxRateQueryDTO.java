package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-18
 * @Description :   TaxRate查询from对象
 * @Modified :
 */
@ApiModel("TaxRate查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaxRateQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "金额类型")
    private String fundType;

    @ApiModelProperty(value = "是否有效(0:无效,1:有效)")
    private String enableFlag;

    @ApiModelProperty(value = "生效时间")
    private LocalDate enableDate;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "租赁细类")
    private String leaseSubType;
}
