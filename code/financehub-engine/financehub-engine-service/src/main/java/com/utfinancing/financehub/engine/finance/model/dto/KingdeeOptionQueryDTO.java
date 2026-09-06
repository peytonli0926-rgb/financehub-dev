package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :   AccountAssistBalance查询from对象
 * @Modified :
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeOptionQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;

    @ApiModelProperty(value = "核算项目类型(搜索核算项目时用)")
    private String asstType;
}
