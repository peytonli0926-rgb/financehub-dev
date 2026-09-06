package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-11
 * @Description : 长期应收款登记VO excel对象
 * @Modified :
 */
@Data
public class LongReceivableRegisterExcelVO implements Serializable {
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

    @ApiModelProperty(value = "客户编号")
    @Excel(name = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体")
    private String orgName;

    @ApiModelProperty(value = "应收总额")
    @Excel(name = "应收总额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableTotal;

    @ApiModelProperty(value = "应收本金")
    @Excel(name = "应收本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    @Excel(name = "应收利息", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "其他应收款")
    @Excel(name = "其他应收款", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal otherReceivable;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;

}
