package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 线下合同租金计划DTO对象
 * @Modified :
 */
@Data
public class OfflineContractRepaymentPlanExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "期数")
    private Integer period;

    @Excel(name = "还款日", cellType = Excel.ColumnType.DATE)
    private Date planDate;

    @Excel(name = "应收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentAmount;

    @Excel(name = "本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal principalAmount;

    @Excel(name = "利息", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal interestAmount;

    @Excel(name = "现金流", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal cashFlow;


}
