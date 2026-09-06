package com.utfinancing.financehub.etl.constant;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.constant.CommonConstant</li>
 * <li>CreateTime : 2023/11/10 15:02</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface CommonConstant {

    /**
     * 开票认领字典类型
     */
    String INVOICE_CLAIM_DICT_TYPE="invoice_claim";

    /**
     * 开票认领签约实体字典类型
     */
     String INVOICE_CLAIM_ORG_ID_DICT_TYPE = "invoice_claim_org_id";

    /**
     * 金融服务、融资租赁大类
     */
     String INCOICE_CLAIM_DICT_FINANCIAL_LEASE = "金融服务/融资租赁";

    /**
     * 系统来源-乘用车系统
     */
    String CYCXT="CYCXT";

    /**
     * 系统来源-商用车系统
     */
    String SYCXT="SYCXT";

    /**
     * 1.开票主体、开票金额、开票税率、开票对象不一致。
     */
    String INVOICE_ERROR_COMMENT = "开票主体、开票金额、开票税率、开票对象不一致。";

    /**
     * 2.累计已开金额大于已收款金额
     */
    String INVOICE_AMOUNT_ERROR_COMMENT = "累计已开金额大于已收款金额";

    /**
     * 开票来源
     */
    String INVOICE_BUSINESS_SOURCE = "invoice_business_source";

}
