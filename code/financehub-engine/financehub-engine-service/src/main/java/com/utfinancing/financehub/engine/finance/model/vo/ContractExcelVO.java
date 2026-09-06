package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ContractExcelVO {

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "系统名称")
    @Excel(name = "系统名称",width = 20)
    private String systemCodeName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编码",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "合同状态")
    @Excel(name = "合同状态",width = 20)
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "业务日期")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDate businessDate;


    @ApiModelProperty(value = "起租日")
    @Excel(name = "起租日",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDate leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @Excel(name = "到期日",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDate leaseDateEnd;


}
