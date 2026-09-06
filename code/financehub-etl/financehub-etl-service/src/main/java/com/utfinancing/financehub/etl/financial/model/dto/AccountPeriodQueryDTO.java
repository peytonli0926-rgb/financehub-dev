package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :   AccountPeriod查询from对象
 * @Modified :
 */
@ApiModel("AccountPeriod查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountPeriodQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "期间编码")
    private Integer periodCode;

    @ApiModelProperty(value = "会计年度")
    private Integer periodYear;

    @ApiModelProperty(value = "会计季度")
    private Integer periodQuarter;

    @ApiModelProperty(value = "期间")
    private Integer periodNumber;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "期间名称")
    private String periodName;

    @ApiModelProperty(value = "金蝶主键ID")
    private String easId;
}
