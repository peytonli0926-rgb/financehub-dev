package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :   LprData查询from对象
 * @Modified :
 */
@ApiModel("LprData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LprDataQueryDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "利率参照年限")
    private String period;

    @ApiModelProperty(value = "利率")
    private BigDecimal lprRate;
}
