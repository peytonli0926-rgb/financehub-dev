package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportContractBalanceExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "年月", type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @Excel(name = "公司编号", type = Excel.Type.IMPORT)
    private String orgId;

    @Excel(name = "合同号", type = Excel.Type.IMPORT)
    private String contractCode;
    @Excel(name = "科目代码", type = Excel.Type.IMPORT)
    private String accountCode;
    @Excel(name = "分类", type = Excel.Type.IMPORT)
    private String businessCode;
    @Excel(name = "客户编号", type = Excel.Type.IMPORT)
    private String clientCode;
    @Excel(name = "应收租金", type = Excel.Type.IMPORT)
    private BigDecimal receivableRentBalance;
    @Excel(name = "应收首付款", type = Excel.Type.IMPORT)
    private BigDecimal receivableDownpaymentBalance;
    @Excel(name = "应收期末残值", type = Excel.Type.IMPORT)
    private BigDecimal receivableResidualValueBalance;
    @Excel(name = "应收手续费", type = Excel.Type.IMPORT)
    private BigDecimal receivableCommissionBalance;
    @Excel(name = "应收保险费", type = Excel.Type.IMPORT)
    private BigDecimal receivableInsuranceBalance;
    @Excel(name = "应收其他收入", type = Excel.Type.IMPORT)
    private BigDecimal receivableOtherincomeBalance;
    @Excel(name = "应收返利", type = Excel.Type.IMPORT)
    private BigDecimal receivableRebateBalance;
    @Excel(name = "应收销项税", type = Excel.Type.IMPORT)
    private BigDecimal receivableOuttaxBalance;
    @Excel(name = "未确认收款", type = Excel.Type.IMPORT)
    private BigDecimal receivableUnconfirmReceiptBalance;
    @Excel(name = "未实现收益", type = Excel.Type.IMPORT)
    private BigDecimal unrealizedRevenueBalance;
    @Excel(name = "组合拨备余额", type = Excel.Type.IMPORT)
    private BigDecimal depreciationReservesBalance;
    @Excel(name = "承租人保证金", type = Excel.Type.IMPORT)
    private BigDecimal lesseeMarginBalance;
    @Excel(name = "应付设备款", type = Excel.Type.IMPORT)
    private BigDecimal payableDeviceBalance;
    @Excel(name = "2023-6-30余额", type = Excel.Type.IMPORT)
    private BigDecimal balance;
    @Excel(name = "应付其他租赁成本余额", type = Excel.Type.IMPORT)
    private String payableOtherCostBalance;


}
