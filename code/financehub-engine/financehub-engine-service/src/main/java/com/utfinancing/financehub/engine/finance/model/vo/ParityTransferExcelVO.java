package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ParityTransferExcelVO</li>
 * <li>CreateTime : 2024/04/09 16:25</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("平价转让模板VO")
@Data
public class ParityTransferExcelVO {

    @ApiModelProperty(value = "批次")
    @Excel(name = "转让批次",width = 20)
    private String batch;

    @ApiModelProperty(value = "转让方")
    @Excel(name = "转让方",width = 20)
    private String transferParty;

    @ApiModelProperty(value = "受让方")
    @Excel(name = "受让方",width = 20)
    private String transfereeParty;

    @ApiModelProperty(value = "基准日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "基准日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date referenceDate;

    @ApiModelProperty(value = "交易日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date tradeDate;

    @ApiModelProperty(value = "转让价格")
    @Excel(name = "转让价格",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同数量")
    @Excel(name = "合同数量",width = 20)
    private Integer contractNum;

    @ApiModelProperty(value = "转让后是否开发票（0：否，1：是）")
    @Excel(name = "转让后是否开发票",width = 20,readConverterExp = "0=否,1=是")
    private String isInvoiceFlag;
}
