package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ChargeOffModifyExcelDTO</li>
 * <li>CreateTime : 2024/02/27 15:14</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("ChargeOff修改导出ExcelDTO")
@Data
public class ChargeOffModifyExcelDTO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "核销时间")
    @Excel(name = "核销时间(yyyy-MM-dd)",width = 20,dateFormat = "yyyy-MM-dd")
    private Date verificationDate;

    @ApiModelProperty(value = "核销状态")
    @Excel(name = "核销状态",width = 20)
    private String verificationStatus;

    @ApiModelProperty(value = "财务核销敞口")
    @Excel(name = "财务核销敞口",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "拨备转回年份")
    @Excel(name = "拨备转回年份",width = 20)
    private String provisionReversalYear;

    @ApiModelProperty(value = "拨备转回金额")
    @Excel(name = "拨备转回金额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionReversalAmount;

    @ApiModelProperty(value = "税务核销日期")
    @Excel(name = "税务核销日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date taxVerificationDate;

    @ApiModelProperty(value = "税务核销金额")
    @Excel(name = "税务核销金额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxVerificationAmount;
}
