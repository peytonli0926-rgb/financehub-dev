package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CheckTypeEnum</li>
 * <li>CreateTime : 2023/10/12 09:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
//对账记录状态
public enum BusinessSceneEnum {
    //leaseTable:租赁大表
    LEASE_TABLE("leaseTable", "租赁大表"),

    SPECIAL_CONTRACT_SUMMARY("special_contract_summary", "特殊合同状态汇总表"),

    BATCH_QUERY("batchQuery", "查询功能"),

    INBOUND_OUTBOUND("inboundOutbound", "财务入库出库报表"),

    PAYABLE_INSURANCE("payableInsurance", "应付保险费"),

    MARGIN_CONTRACT("marginContract", "保证金"),

    CHARGE_OFF_SUMMARY("charge_off_summary", "chargeOff汇总表"),

    ASSIST_BALANCE("assist_balance", "核算项目余额表"),

    VERIFICATION_SUMMARY("verification_summary", "核销回款汇总模块"),

    ;

    private final String code;
    private final String desc;

    BusinessSceneEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (BusinessSceneEnum enums : BusinessSceneEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static BusinessSceneEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (BusinessSceneEnum enums : BusinessSceneEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
