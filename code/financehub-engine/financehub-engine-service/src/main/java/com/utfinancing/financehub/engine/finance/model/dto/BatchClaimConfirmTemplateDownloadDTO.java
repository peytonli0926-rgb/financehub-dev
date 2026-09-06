package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BatchClaimConfirmTemplateDownloadDTO implements Serializable {
    @Excel(name = "到账主体")
    private String collectionAccountsBank;

    @Excel(name = "认领主体", comment = "必填")
    private String OrgName;

    @Excel(name = "记账日期", comment = "记账日期(格式：yyyy-MM-dd)", dateFormat = "yyyy-MM-dd", cellType = Excel.ColumnType.DATE)
    private Date accountDate;

    @Excel(name = "业务系统网银编号-小网银", comment = "必填")
    private String ebankSerialNumber;

    @Excel(name = "网银到账金额", comment = "网银到账金额")
    private BigDecimal bankAmount;

    @Excel(name = "剩余未确认金额", comment = "剩余未确认金额")
    private BigDecimal remainNonConfirmAmount;

    @Excel(name = "认领金额", comment = "必填")
    private BigDecimal claimAmount;

    @Excel(name = "金额类型", comment = "必填", combo = {"回收租金（回收本金）","收取首付款", "收取留购价", "收取手续费",
    "收取厂商返利", "应收保险费", "收取其他收入", "收取服务费", "收取履约保证金", "收取供应商保证金", "回收罚息", "收取合同解约及更改手续费",
    "收取违约金", "应收诉讼费", "诉讼费", "罚没"})
    private String amountType;

    @Excel(name = "罚没业务类型", comment = "罚没业务类型（金额类型选择罚没时填写）", combo = {"罚没租赁相关","罚息", "其他"})
    private String confiscateBusinessType;

    @Excel(name = "诉讼费类型", comment = "诉讼费类型（金额类型选择诉讼费时填写）", combo = {"收回转让前","收回转让后"})
    private String briefFeeType;

    @Excel(name = "合同号", comment = "合同号")
    private String contractCode;

    @Excel(name = "客户", comment = "客户")
    private String clientCode;

    @Excel(name = "是否涉及其他客户及辅助帐", comment = "必填", combo = {"是","否"})
    private String isRelateClientAuxiliaryAccount;

    @Excel(name = "借款合同编号")
    private String billContractCode;

    @Excel(name = "银行账号")
    private String ebankNum;
}
