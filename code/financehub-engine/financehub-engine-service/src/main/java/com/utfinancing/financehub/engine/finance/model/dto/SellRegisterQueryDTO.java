package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description :   SellRegister查询from对象
 * @Modified :
 */
@ApiModel("SellRegister查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SellRegisterQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "资产编号")
    private String assetNumber;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "ID")
    private Long id;
}
