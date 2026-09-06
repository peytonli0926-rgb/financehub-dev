package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :   RecyclingEquipmentIn查询from对象
 * @Modified :
 */
@ApiModel("最近合同余额表数据查询")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBalanceLastQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "凭证日期")
    private String voucherDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同代码集合")
    private List<String> contractCodeList;

    private Integer periodCode;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;
}
