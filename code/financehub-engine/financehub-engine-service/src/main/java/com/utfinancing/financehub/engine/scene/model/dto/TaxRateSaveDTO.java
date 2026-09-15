package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-18
 * @Description : 税率配置表DTO对象
 * @Modified :
 */
@Data
public class TaxRateSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "金额类型")
    private String fundType;

    @ApiModelProperty(value = "资产类别（可选：动产/不动产）")
    private String assetCategory;

    @ApiModelProperty(value = "生效时间")
    private LocalDate enableDate;

    @ApiModelProperty(value = "是否有效(0:无效,1:有效)")
    private String enableFlag;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "租赁细类")
    private String leaseSubType;

}
