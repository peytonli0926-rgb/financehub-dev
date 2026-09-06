package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectNonConfirmCollectionSumByPageDTO extends NonConfirmCollectionSumEntity implements Serializable {
    @ApiModelProperty(value = "已确认金额")
    private BigDecimal claimAmount;

    @ApiModelProperty(value = "剩余未确认金额")
    private BigDecimal remainNonConfirmAmount;

    //认领主体
    @ApiModelProperty(value = "认领主体")
    private String orgId;

    //认领主体中文名称
    @ApiModelProperty(value = "认领主体中文名称")
    private String orgName;
}
