package com.utfinancing.financehub.engine.finance.constant;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public interface DefaultConstant {

    String DEBIT_CREDIT_TYPE_DR = "DR"; //借
    String DEBIT_CREDIT_TYPE_CR = "CR"; //贷

    BigDecimal DEFAULT_LPR = new BigDecimal("4.2");

    /**
     * 科目为1999.99允许编辑
     */
    public static String EDIT_FLAG_ACCOUNT_CODE="1999.99";

    /**
     * 销项税金额
     */
    String OUTTAX = "outtax";

    /**
     * 细分场景35
     */
    String SUB_SCENE_CODE_35 = "35";

    List<String> SUB_SCENE_CODE_List = Arrays.asList("7", "25");

    /**
     * 2241.02 其他应付款_关联公司往来
     */
    String BANK_PAYABLE_RELATED_PARTY  = "2241.02";

    //2241.17 其他应付款_代收转让款项
    String BANK_COLLECTION_TRANSFER  = "2241.17";


    /**
     * 1221.01 其他应收款_关联公司往来
     */
    String ORG_RECEIVABLE_RELATED_PARTY = "1221.01";

    //1221.15 其他应收款_应收转让后收款
    String ORG_RECEIVABLE_COLLECTION_TRANSGER = "1221.15";

    //1221.99 其他应收款
    String OTHER_RECEIVABLE_COLLECTION_TRANSGER = "1221.99";

    /**
     * 客户类型承租人
     */
    String CLIENT_TYPE_LESSEE = "承租人";

    /**
     * 默认客户编码
     */
    String DEFAULT_CLIENT_CODE = "999";

    /**
     * 客户类型-个人
     */
    String PERSON = "个人";

    /**
     * 客户类型-法人
     */
    String LEGAL_PERSON = "法人";

    String ACCRUE = "计提";

    String INVOICE = "开票";
}
