package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BatchClaimConfirmDTO implements Serializable {
    @Excel(name = "到账主体", comment = "到账主体")
    private String collectionAccountsBank;

    @Excel(name = "认领主体", comment = "认领主体")
    private String OrgName;

    private NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity;

    private String orgId;

    @Excel(name = "记账日期", comment = "记账日期(格式：yyyy-MM-dd)", dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @Excel(name = "业务系统批扣流水号", comment = "业务系统批扣流水号")
    private String ebankSerialNumber;

    @Excel(name = "网银到账金额", comment = "网银到账金额")
    private BigDecimal bankAmount;

    @Excel(name = "剩余未确认金额", comment = "剩余未确认金额")
    private BigDecimal remainNonConfirmAmount;

    @Excel(name = "认领金额", comment = "认领金额")
    private BigDecimal claimAmount;

    @Excel(name = "金额类型", comment = "金额类型")
    private String amountType;

    @Excel(name = "罚没业务类型", comment = "罚没业务类型（金额类型选择罚没时填写）")
    private String confiscateBusinessType;

    @Excel(name = "诉讼费类型", comment = "诉讼费类型（金额类型选择诉讼费时填写）")
    private String briefFeeType;

    @Excel(name = "合同号", comment = "合同号")
    private String contractCode;

    private ContractDTO contractDTO;

    @Excel(name = "客户", comment = "客户")
    private String clientCode;

    @Excel(name = "是否涉及其他客户及辅助帐", comment = "是否涉及其他客户及辅助帐", combo = "{是,否}")
    private String isRelateClientAuxiliaryAccount;

    @Excel(name = "银行账号")
    private String ebankNum;

    @Excel(name = "借款合同编号")
    private String billContractCode;


    private ClientEntity clientEntity;

    // 错误信息
    @Excel(name = "错误信息", type = Excel.Type.EXPORT)
    private String errs;
}
