package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailExcelVO</li>
 * <li>CreateTime : 2024/04/09 16:29</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("平价转让详情ExcelVO")
@Data
public class ParityTransferDetailExcelVO {

    @ApiModelProperty(value = "转让批次")
    @Excel(name = "转让批次",width = 20)
    private String batch;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "评估价")
    @Excel(name = "评估价",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal appraisedValue;

}
