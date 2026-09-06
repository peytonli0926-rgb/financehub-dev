package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ParityTransferExportVO</li>
 * <li>CreateTime : 2024/04/02 17:10</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
@ApiModel("平价转让导出VO")
public class ParityTransferExportVO {

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "批次")
    @Excel(name = "转让批次",width = 20)
    private String batch;

    @ApiModelProperty(value = "转让方")
    @Excel(name = "转让方",width = 20)
    private String transferPartyName;

    @ApiModelProperty(value = "受让方")
    @Excel(name = "受让方",width = 20)
    private String transfereePartyName;

    @ApiModelProperty(value = "基准日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "基准日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date referenceDate;

    @ApiModelProperty(value = "交易日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "基准日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date tradeDate;

    @ApiModelProperty(value = "转让价格")
    @Excel(name = "转让价格",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同数量")
    @Excel(name = "合同数量",width = 20)
    private Integer contractNum;

    @ApiModelProperty(value = "转让后是否开发票（0：否，1：是）")
    @Excel(name = "转让后是否开发票",width = 20)
    private String isInvoiceFlag;

    @ApiModelProperty(value = "支付日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "支付日期",dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    @ApiModelProperty(value = "银行账号编码")
    @Excel(name = "银行账号编码")
    private String bankAccountCode;
}
