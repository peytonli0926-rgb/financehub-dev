package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description :   CheckCommonFinanceData查询from对象
 * @Modified :
 */
@ApiModel("CheckCommonFinanceData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckCommonFinanceDataQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "执行日期 分区字段")
    private LocalDateTime executeDate;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "关联字段")
    private String joinField;

    @ApiModelProperty(value = "查询字段")
    private String queryField;

    @ApiModelProperty(value = "对接业务系统代码")
    private String dbCode;

    @ApiModelProperty(value = "展示字段")
    private String showField;
}
