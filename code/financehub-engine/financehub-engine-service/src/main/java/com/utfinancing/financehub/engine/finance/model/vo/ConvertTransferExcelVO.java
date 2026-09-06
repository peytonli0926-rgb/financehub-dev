package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-汇总VO对象
 * @Modified :
 */
@Data
public class ConvertTransferExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次")
    @Excel(name = "批次")
    private String batch;

    @ApiModelProperty(value = "业务日期")
    @Excel(name = "业务日期",dateFormat = "yyyy-MM-dd")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "财务日期")
    @Excel(name = "财务日期",dateFormat = "yyyy-MM-dd")
    private LocalDateTime financeDate;

    @ApiModelProperty(value = "转让方")
    @Excel(name = "转让方")
    private String transferParty;

    @ApiModelProperty(value = "受让方")
    @Excel(name = "受让方")
    private String transfereeParty;

    @ApiModelProperty(value = "基准日")
    @Excel(name = "基准日",dateFormat = "yyyy-MM-dd")
    private LocalDateTime referenceDate;

    @ApiModelProperty(value = "交易日")
    @Excel(name = "交易日",dateFormat = "yyyy-MM-dd")
    private LocalDateTime tradeDate;

    @ApiModelProperty(value = "转让价格")
    @Excel(name = "转让价格")
    private String transferPrice;

    @ApiModelProperty(value = "合同数量")
    @Excel(name = "合同数量")
    private Integer contractNum;

    @ApiModelProperty(value = "转让后是否开发票（0：否，1：是）")
    @Excel(name = "转让后是否开发票",readConverterExp = "0=否,1=是")
    private String isInvoiceFlag;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;
    

}
