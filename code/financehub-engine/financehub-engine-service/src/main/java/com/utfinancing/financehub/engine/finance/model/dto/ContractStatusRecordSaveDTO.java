package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 合同状态记录表DTO对象
 * @Modified :
 */
@Data
public class ContractStatusRecordSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号（必填）", type = Excel.Type.IMPORT)
    private String contractCode;

    @ApiModelProperty(value = "公司")
    @Excel(name = "签约主体（必填）", type = Excel.Type.IMPORT)
    private String orgId;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态（必填）", type = Excel.Type.IMPORT)
    private String financialContractStatus;

    @ApiModelProperty(value = "财务合同状态更新时间")
    @Excel(name = "财务合同状态更新时间（必填、日期格式）", type = Excel.Type.IMPORT, cellType = Excel.ColumnType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date financialContractStatusUpdateTime;


    @ApiModelProperty(value = "转入公司")
    @Excel(name = "转入公司", type = Excel.Type.IMPORT)
    private String transferOrgId;

    @ApiModelProperty(value = "转入合同号")
    @Excel(name = "转入合同号", type = Excel.Type.IMPORT)
    private String transferContractCode;

    @ApiModelProperty(value = "转入合同系统合同状态")
    @Excel(name = "转入合同系统合同状态", type = Excel.Type.IMPORT)
    private String transferContractStatus;

}
