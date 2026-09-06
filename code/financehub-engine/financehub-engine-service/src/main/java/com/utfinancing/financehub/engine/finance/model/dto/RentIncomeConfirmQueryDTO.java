package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description :   RentIncomeConfirm查询from对象
 * @Modified :
 */
@ApiModel("RentIncomeConfirm查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RentIncomeConfirmQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @JsonFormat(pattern = "yyyy-MM", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM")
    @ApiModelProperty(value = "记账月份（yyyy-MM）")
    private Date accountMonth;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;
}
