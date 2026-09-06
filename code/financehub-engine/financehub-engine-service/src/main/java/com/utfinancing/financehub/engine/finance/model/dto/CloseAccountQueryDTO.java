package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-16
 * @Description :   CloseAccount查询from对象
 * @Modified :
 */
@ApiModel("CloseAccount查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CloseAccountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "系统来源（统一平台：TYPT,商用车系统：SYCXT,小微系统：XWXT,乘用车系统:CYCXT）	")
    private String systemCode;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "关账日期")
    private String closeDate;

    @ApiModelProperty(value = "关账操作日期")
    private String operateDate;

    @ApiModelProperty(value = "关账人")
    private String operateUser;

    @ApiModelProperty(value = "状态")
    private String status;
}
