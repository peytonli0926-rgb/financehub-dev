package com.utfinancing.financehub.engine.enums;

/**
 * @Author : lixin
 * @Date : Create in 17/12/2023
 */
public enum DwDictTypeEnum {

    YEWZL("yewzl", "业务子类"),
    HAITHYFLBQ("haithyflbq", "[内部标签]行业分类标签"),
    HAITKHDXBQ("haitkhdxbq", "[内部标签]客户大小标签"),
    HAITNBKHXZBQ("haitnbkhxzbq", "[内部标签]内部客户性质"),
    TIAOXFS("tiaoxfs", "调息方式"),

    ;

    private final String code;
    private final String desc;

    DwDictTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
