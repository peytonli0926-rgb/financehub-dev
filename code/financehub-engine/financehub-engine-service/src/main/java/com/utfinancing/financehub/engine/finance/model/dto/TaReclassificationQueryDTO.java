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
 * @Date : Create in 2024-05-24
 * @Description :   TaReclassification查询from对象
 * @Modified :
 */
@ApiModel("TaReclassification查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaReclassificationQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "重分类月份")
    private String reclassificationMonth;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "TA重分类金额 为0 :0 不为0 :1")
    private String taReclassificationAmount;

}
