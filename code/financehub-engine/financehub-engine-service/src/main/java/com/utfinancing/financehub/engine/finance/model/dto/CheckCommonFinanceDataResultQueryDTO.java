package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description :   CheckCommonFinanceDataResult查询from对象
 * @Modified :
 */
@ApiModel("CheckCommonFinanceDataResult查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckCommonFinanceDataResultQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记录ID")
    private Long recordId;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "对接业务系统代码")
    private String dbCode;

    @ApiModelProperty(value = "common参数")
    private Map<String, Object> param;

}
