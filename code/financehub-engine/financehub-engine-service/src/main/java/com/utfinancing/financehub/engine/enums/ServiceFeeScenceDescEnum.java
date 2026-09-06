package com.utfinancing.financehub.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ServiceFeeScenceDescEnum {

    ZXFWFQY("ZXFWFQY", "咨询服务费签约", "YYPT"),
    ZXFWQY("BZJCFL", "咨询服务签约", "TYPT"),
    ZXFWF("BZJCFL", "咨询服务费", "XWXT"),
    ZXFWFSK("BZJCFL", "咨询服务费收款", "YYPT,TYPT"),
    HL("BZJCFL", "回笼", "XWXT"),
    ;


    private String code;
    private String desc;
    private String systemCode;

    /**
     * 获取签约场景描述
     *
     */
    public static List<String> getSignDesc() {
        List<String> signs = new ArrayList<>();
        signs.add(ZXFWFQY.getDesc());
        signs.add(ZXFWQY.getDesc());
        signs.add(ZXFWF.getDesc());
        return signs;
    }


}
