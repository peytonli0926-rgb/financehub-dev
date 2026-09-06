package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ChargeOffExcelDTO</li>
 * <li>CreateTime : 2024/02/29 14:20</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "Charge Off 导出DTO")
@Data
public class ChargeOffExcelDTO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号", width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体", width = 20)
    private String orgName;

    @ApiModelProperty(value = "核销状态")
    @Excel(name = "核销状态", width = 20)
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称", width = 20)
    private String clientName;

    @ApiModelProperty(value = "核销时间")
    @Excel(name = "核销时间", width = 20, dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private Date verificationDate;

    @ApiModelProperty(value = "核销时间")
    @Excel(name = "核销时间", width = 20)
    private String verificationDateString;

    @ApiModelProperty(value = "财务核销敞口")
    @Excel(name = "财务核销敞口", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "X-10年以前拨备转回金额")
    @Excel(name = "X-10年以前拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount10;

    @ApiModelProperty(value = "X-9年拨备转回金额")
    @Excel(name = "X-9年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount9;

    @ApiModelProperty(value = "X-8年拨备转回金额")
    @Excel(name = "X-8年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount8;

    @ApiModelProperty(value = "X-7年拨备转回金额")
    @Excel(name = "X-7年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount7;

    @ApiModelProperty(value = "X-6年拨备转回金额")
    @Excel(name = "X-6年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount6;

    @ApiModelProperty(value = "X-5年拨备转回金额")
    @Excel(name = "X-5年以前拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount5;

    @ApiModelProperty(value = "X-4年拨备转回金额")
    @Excel(name = "X-4年以前拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount4;

    @ApiModelProperty(value = "X-3年拨备转回金额")
    @Excel(name = "X-3年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount3;

    @ApiModelProperty(value = "X-2年拨备转回金额")
    @Excel(name = "X-2年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount2;

    @ApiModelProperty(value = "X-1年拨备转回金额")
    @Excel(name = "X-1年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount1;

    @ApiModelProperty(value = "X年拨备转回金额")
    @Excel(name = "X年拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount0;

    @ApiModelProperty(value = "拨备转回金额")
    private BigDecimal provisionReversalAmount;

    @ApiModelProperty(value = "拨备转回金额汇总")
    @Excel(name = "拨备转回金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmountTotal;

    @ApiModelProperty(value = "拨备转回年份")
    private String provisionReversalYear;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    //坏账核销余额=财务核销敞口-累计拨备转回
    @ApiModelProperty(value = "坏账核销余额")
    @Excel(name = "坏账核销余额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal badDebtWriteOffBalance;

    @ApiModelProperty(value = "税务核销日期")
    @Excel(name = "税务核销日期", width = 20, dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private Date taxVerificationDate;

    @ApiModelProperty(value = "税务核销日期")
    @Excel(name = "税务核销日期", width = 20, dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private String taxVerificationDateString;

    @ApiModelProperty(value = "税务核销金额")
    @Excel(name = "税务核销金额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxVerificationAmount;

}
