package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :   CheckCommon 对账对接业务系统统一触发对账传输dto
 * @Modified :
 */
@ApiModel("CheckCommon实时对账统一触发")
@Data
public class CheckCommonQueryDTO {

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

//    @ApiModelProperty(value = "业务系统代码")
//    private String systemCode;

    @ApiModelProperty(value = "业务场景 科目明细余额:Detail 金蝶科目余额:Kingdee 金蝶中间表发生额:Middle 实付保险费:shifbxf 应收销项税:ysxxs")
    private String businessType;

}
