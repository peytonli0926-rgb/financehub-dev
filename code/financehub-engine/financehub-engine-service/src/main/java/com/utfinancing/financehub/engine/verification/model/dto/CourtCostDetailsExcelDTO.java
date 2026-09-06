package com.utfinancing.financehub.engine.verification.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsExcelDTO</li>
 * <li>CreateTime : 2023/10/24 10:08</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "诉讼费导入DTO")
@Data
public class CourtCostDetailsExcelDTO {

    @ApiModelProperty(value = "记账日期")
    @Excel(name = "记账日期", cellType = Excel.ColumnType.DATE, width = 20)
    private Date accountDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体名称", width = 20)
    private String orgName;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号", width = 20)
    private String contractCode;

    @ApiModelProperty(value = "转费用金额")
    @Excel(name = "转费用金额", width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal transgerCostAmount;

    @ApiModelProperty(value = "成本中心")
    @Excel(name = "成本中心", width = 20)
    private String costCenter;
}
