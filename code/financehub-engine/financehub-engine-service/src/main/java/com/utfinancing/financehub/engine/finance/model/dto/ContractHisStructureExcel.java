package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 合同修改交易数据
 * @Modified :
 */
@Data
public class ContractHisStructureExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "主合同编号")
    private String contractCodeM;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "设备款", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableDeviceAmount;

    @Excel(name = "首付款", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableFirstAmount;

    @Excel(name = "出租人保险费", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lessorInsuranceAmount;

    @Excel(name = "承租人履约保证金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableMarginAmount;

    @Excel(name = "渠道费用", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal channelFees;

    @Excel(name = "手续费收入(含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableProcedureAmount;

    @Excel(name = "出租人其它成本", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lessorOtherCosts;

    @Excel(name = "厂商返利", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableFirmRebate;

    @Excel(name = "承租人保险费", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInsuranceAmount;

    @Excel(name = "期末残值", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal retainedPrice;

    @Excel(name = "其他收入 (含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOther;

    @Excel(name = "咨询服务收入(含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableServiceAmount;

    @Excel(name = "供应商履约保证金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal vendorMarginAmount;

}
