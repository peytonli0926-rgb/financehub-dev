package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentSummarExcelVO</li>
 * <li>CreateTime : 2024/03/21 15:24</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class AssetAbsTransferPaymentSummarExcelVO {

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "出表期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    //=实付本金+实付利息
    @ApiModelProperty(value = "代收款项_租金")
    @Excel(name = "出表期数",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivablesRentalAmount;

    //=实付留够价
    @ApiModelProperty(value = "代收款项_残值")
    @Excel(name = "代收款项_残值",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRetentionPurchaseAmount;

    //=实付罚息及手续费
    @ApiModelProperty(value = "代收款项_其他")
    @Excel(name = "代收款项_其他",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualPenaltyInterestAmount;

}
