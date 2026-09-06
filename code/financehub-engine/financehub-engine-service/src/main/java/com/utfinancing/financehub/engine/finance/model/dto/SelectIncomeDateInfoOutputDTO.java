package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectIncomeDateInfoOutputDTO implements Serializable {
    private String id;
    private String systemCode;
    private String businessEbankNumber;
    private String ebankSerialNumber;
    private BigDecimal claimAmount;
    private String processStatus;
    private String businessVoucherIds;
    private Date businessDate;
    //客户编号
    private String clientCode;
    //合同号
    private String contractCode;

    //币种
    private String currencyType;
    //业务系统交易流水号
    private String orderId;
    // 签约主体
    private String orgId;

    // 签约主体名称
    private String orgName;
    // 新业务系统批扣流水号
    private String newEbankSerialNumber;
    // 是否存在跨主体(0;否;1:是)
    private String isCrossOrg;

    private Long detailId;

    private Long sumId;

    private String systemName;

    private Date systemDate;

    private Date businessHappenDate;

    //网银到账金额
    private BigDecimal bankAmount;

    //剩余未确认金额
    private BigDecimal remainNonConfirmAmount;

    //已确认金额
    private BigDecimal confirmAmount;

    private BigDecimal curClaimAmount;
    // 是否涉及其他客户及辅助帐
    private String isRelateClientAuxiliaryAccount;

    private String voucherIds;
    private String writeOffVoucherId;
    private String manualVoucherIds;
    private BigDecimal claimAmountTotal;
}
