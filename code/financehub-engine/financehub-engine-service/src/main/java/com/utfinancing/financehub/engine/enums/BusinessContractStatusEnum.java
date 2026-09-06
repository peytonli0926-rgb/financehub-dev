package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 合同状态
 */
public enum BusinessContractStatusEnum {
    CONTRACT_STATUS_1("正常起租", "正常起租"),
    CONTRACT_STATUS_2("合同结束", "合同结束"),
    CONTRACT_STATUS_3("财务入库", "财务入库"),
    CONTRACT_STATUS_4("合同签约", "合同签约"),
    CONTRACT_STATUS_5("合同撤销", "合同撤销"),


    ;
    private final String code;
    private final String desc;

    BusinessContractStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 取得有效合同状态列表
     */
    public static List<String> getValidStatus () {
        List<String> result = new ArrayList<>();
        result.add(CONTRACT_STATUS_1.getCode());
        result.add(CONTRACT_STATUS_3.getCode());
        result.add(CONTRACT_STATUS_4.getCode());
        return result;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
