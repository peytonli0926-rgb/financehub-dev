package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ChargeOffAddExcelDTO</li>
 * <li>CreateTime : 2024/02/27 15:06</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("ChargeOff新增导出ExcelDTO")
@Data
public class ChargeOffAddExcelDTO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "核销状态")
    @Excel(name = "核销状态",width = 20)
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "核销时间")
    @Excel(name = "核销时间(yyyy-MM-dd)",width = 20,dateFormat = "yyyy-MM-dd")
    private Date verificationDate;

    @ApiModelProperty(value = "财务核销敞口")
    @Excel(name = "财务核销敞口",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "拨备转回年份")
    @Excel(name = "拨备转回年份",width = 20)
    private String provisionReversalYear;

    @ApiModelProperty("会计期间")
    @Excel(name = "会计期间",width = 20)
    private Integer periodCode;

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
