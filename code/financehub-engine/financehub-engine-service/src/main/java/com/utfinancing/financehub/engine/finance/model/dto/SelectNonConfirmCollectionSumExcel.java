package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectNonConfirmCollectionSumExcel implements Serializable {

    //到账主体
    @Excel(name = "到账主体")
    private String collectionAccountsBank;

    //网银编号-资金系统
    @Excel(name = "资金系统网银编号")
    private String ebankNumber;

    //认领主体
    @Excel(name = "认领主体")
    private String orgName;

    //业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
    @Excel(name = "业务系统网银编号")
    private String ebankSerialNumber;

    //到账银行账号
    @Excel(name = "到账银行账号")
    private String collectionAccountsBankNo;

    // 网银到账金额
    @Excel(name = "网银到账金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal bankAmount;

    // 应批扣金额
    @Excel(name = "应批扣金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accountsReceivable;

    // 剩余未确认金额
    @Excel(name = "剩余未确认金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal remainNonConfirmAmount;

    @Excel(name = "已确认金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal claimAmount;
}
