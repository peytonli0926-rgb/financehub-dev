package com.utfinancing.financehub.engine.enums;

/**
 * 减值计提上传excel类型
 */
public enum ImpairmentExcelTypeEnum {
    EXCEL_TYPE_1("excel_1", "附件1：拨备减值明细_法人_to 财务"),
    EXCEL_TYPE_2("excel_2", "附件2：拨备减值明细_自然人_to 财务"),
//    EXCEL_TYPE_3("excel_3", "附件3：其他应收款明细（诉讼保全费）"),
//    EXCEL_TYPE_4("excel_4", "附件4：其他应收款明细（其他保证金）"),
    EXCEL_TYPE_5("excel_5", "附件5：应收经营租赁款减值"),
    EXCEL_TYPE_6("excel_6", "附件6：其他应收款项减值"),
    EXCEL_TYPE_7("excel_7", "附件7：长期应收款项减值"),
    EXCEL_TYPE_8("excel_8", "附件8：应收投资性房地产"),
//    EXCEL_TYPE_9("excel_9", "附件9：恒信应收蓬莱租赁款清单"),
    EXCEL_TYPE_10("excel_10", "附件10：其他金融资产"),
    EXCEL_TYPE_11("excel_11", "附件11：减值准备清单_恒运回收设备"),
    EXCEL_TYPE_12("excel_12", "附件12：减值准备清单_恒信（天津）回收设备"),
    EXCEL_TYPE_13("excel_13", "附件13：减值准备清单_抵债资产"),

    ;
    private final String code;
    private final String desc;

    ImpairmentExcelTypeEnum(String code, String desc) {
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
