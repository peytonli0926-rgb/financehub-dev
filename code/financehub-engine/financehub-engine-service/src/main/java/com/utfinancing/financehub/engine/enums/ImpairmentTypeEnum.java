package com.utfinancing.financehub.engine.enums;

/**
 * 减值类型
 */
public enum ImpairmentTypeEnum {
    TYPE_ENUM_1("租赁资产"),
    TYPE_ENUM_2("应收经营租赁"),
    TYPE_ENUM_3("其他应收款"),
    TYPE_ENUM_4("其他应收款项"),
    TYPE_ENUM_5("长期应收款"),
    TYPE_ENUM_6("应收投资性房地产"),
    TYPE_ENUM_7("库存减值"),
    TYPE_ENUM_8("应收关联方租赁款"),
    TYPE_ENUM_9("其他金融资产"),

    //五级分类
    FIVE_CLASS_1("正常"),
    FIVE_CLASS_2("关注类"),
    FIVE_CLASS_3("次级类"),
    FIVE_CLASS_4("可疑类"),
    FIVE_CLASS_5("损失类"),

    //三阶段
    THREE_STEP_1("阶段一"),
    THREE_STEP_2("阶段二"),
    THREE_STEP_3("阶段三"),

    //合计
    TYPE_TOTAL("合计"),

    //业务类型
    BUSINESS_TYPE_1("诉讼保全费"),
    BUSINESS_TYPE_2("其他保证金"),
    BUSINESS_TYPE_3("投资性房地产"),
    BUSINESS_TYPE_4("资产支持专项计划"),
    BUSINESS_TYPE_5("其他金融资产"),
    BUSINESS_TYPE_6("私募债"),

    ;
    private final String code;

    ImpairmentTypeEnum(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }

}
