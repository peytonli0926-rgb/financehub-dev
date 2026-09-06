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
 * @Date : Create in 2024-05-22
 * @Description :   TaOtherPayable查询from对象
 * @Modified :
 */
@ApiModel("TaOtherPayable查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaOtherPayableQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "IDList")
    private List<Long> taOtherPayableIdList;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "重分类月份")
    private String reclassificationMonth;
}
