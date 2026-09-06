package com.utfinancing.financehub.engine.finance.model.vo;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentDetailExcelVO</li>
 * <li>CreateTime : 2024/03/21 15:27</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class AssetAbsTransferPaymentDetailExcelVO{

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "出表期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20)
    private String rate;

    @ApiModelProperty(value = "实付本金")
    @Excel(name = "实付本金",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualPrincipalAmount;

    @ApiModelProperty(value = "实付利息")
    @Excel(name = "实付利息",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualInterestAmount;

    @ApiModelProperty(value = "实付留够价")
    @Excel(name = "实付留够价",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRetentionPurchaseAmount;

    @ApiModelProperty(value = "实付罚息及手续费")
    @Excel(name = "实付罚息及手续费",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualPenaltyInterestAmount;
}
