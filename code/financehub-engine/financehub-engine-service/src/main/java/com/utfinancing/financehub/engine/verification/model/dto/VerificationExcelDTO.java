package com.utfinancing.financehub.engine.verification.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.dto.VerificationExcelDTO</li>
 * <li>CreateTime : 2023/10/12 09:53</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销表模板导出")
@Data
public class VerificationExcelDTO implements Serializable {


    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编码*",width = 20)
    private String contractCode;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "记账日期*（yyyy-MM-dd）", dateFormat = "yyyy-MM-dd",width = 30,cellType = Excel.ColumnType.DATE)
    private Date accountDate;

    @ApiModelProperty(value = "财务核销状态 1：正常核销 2：亏损结清 3：非亏损结清")
    @Excel(name = "财务核销状态*",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "签约主体")
    private String orgId;


}
