package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-汇总DTO对象
 * @Modified :
 */
@Data
public class ConvertTransferExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次")
    @Excel(name = "转让批次")
    private String batch;

    @ApiModelProperty(value = "转让方")
    @Excel(name = "转让方")
    private String transferParty;

    @ApiModelProperty(value = "受让方")
    @Excel(name = "受让方")
    private String transfereeParty;

    @ApiModelProperty(value = "基准日")
    @Excel(name = "基准日",dateFormat = "yyyy-MM-dd")
    private Date referenceDate;

    @ApiModelProperty(value = "交易日")
    @Excel(name = "交易日",dateFormat = "yyyy-MM-dd")
    private Date tradeDate;

    @ApiModelProperty(value = "转让价格")
    @Excel(name = "转让价格")
    private String transferPrice;

    @ApiModelProperty(value = "合同数量")
    @Excel(name = "合同数量")
    private Integer contractNum;

    @ApiModelProperty(value = "转让后是否开发票（0：否，1：是）")
    @Excel(name = "转让后是否开发票",readConverterExp = "0=否,1=是")
    private String isInvoiceFlag;

}
