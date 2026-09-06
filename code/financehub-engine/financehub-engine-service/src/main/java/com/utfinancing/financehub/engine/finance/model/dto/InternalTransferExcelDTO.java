package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description : 资产转让-内部调拨DTO对象
 * @Modified :
 */
@Data
public class InternalTransferExcelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次")
    @Excel(name = "转让批次")
    private String batch;

    @ApiModelProperty(value = "支付日期")
    @Excel(name = "支付日期", dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    @ApiModelProperty(value = "银行账号编码")
    @Excel(name = "银行账号编码", cellType = Excel.ColumnType.STRING)
    private String bankAccountCode;

}
