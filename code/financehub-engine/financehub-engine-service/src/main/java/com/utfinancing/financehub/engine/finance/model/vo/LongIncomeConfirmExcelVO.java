package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-15
 * @Description : 长期应收款-收入确认VO excel对象
 * @Modified :
 */
@Data
public class LongIncomeConfirmExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "长期应收款编号")
    @Excel(name = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "项目名称")
    @Excel(name = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "记账月份")
    @Excel(name = "记账月份", dateFormat = "yyyy-MM")
    private Date accountMonth;

    @ApiModelProperty(value = "确认收入金额")
    @Excel(name = "确认收入金额")
    private BigDecimal confirmIncomeAmount;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;

}
