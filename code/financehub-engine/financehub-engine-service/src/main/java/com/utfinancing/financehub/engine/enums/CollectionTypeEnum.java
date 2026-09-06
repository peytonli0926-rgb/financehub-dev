package com.utfinancing.financehub.engine.enums;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public enum CollectionTypeEnum {
    VC_SHOUKLX01("VC_SHOUKLX01","租赁收入"),
    VC_SHOUKLX02("VC_SHOUKLX02","贷款"),
    VC_SHOUKLX03("VC_SHOUKLX03","票据收入"),
    VC_SHOUKLX04("VC_SHOUKLX04","理财到期"),
    VC_SHOUKLX05("VC_SHOUKLX05","资金调拨划入"),
    VC_SHOUKLX06("VC_SHOUKLX06","其他收入"),
    VC_SHOUKLX07("VC_SHOUKLX07","批量其他收入"),
    VC_SHOUKLX08("VC_SHOUKLX08","活期利息收入"),
    VC_SHOUKLX09("VC_SHOUKLX09","税务补贴"),
    VC_SHOUKLX10("VC_SHOUKLX10","特殊款项"),
    VC_SHOUKLX11("VC_SHOUKLX11","长江供应链"),
    VC_SHOUKLX12("VC_SHOUKLX12","特殊款项-资金"),
    VC_SHOUKLX13("VC_SHOUKLX13","贴现收入"),
    ;
    private final String code;
    private final String desc;

    CollectionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取desc
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CostChannelTypeEnum typeEnum : CostChannelTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return code;
    }

}
