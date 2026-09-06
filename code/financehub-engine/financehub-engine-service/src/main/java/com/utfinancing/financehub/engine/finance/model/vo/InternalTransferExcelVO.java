package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description : 资产转让-内部调拨VO对象
 * @Modified :
 */
@Data
public class InternalTransferExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次")
    @Excel(name = "转让批次")
    private String batch;

    @ApiModelProperty(value = "转让方")
    @Excel(name = "转让方")
    private String transferParty;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "余额")
    @Excel(name = "余额")
    private BigDecimal amount;

    @ApiModelProperty(value = "财务日期")
    @Excel(name = "财务日期",dateFormat = "yyyy-MM-dd")
    private LocalDateTime financeDate;

    @ApiModelProperty(value = "支付日期")
    @Excel(name = "支付日期",dateFormat = "yyyy-MM-dd")
    private LocalDateTime paymentDate;

    @ApiModelProperty(value = "银行账号编码")
    @Excel(name = "银行账号编码")
    private String bankAccountCode;
    
    @ApiModelProperty(value = "处理状态 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;

}
