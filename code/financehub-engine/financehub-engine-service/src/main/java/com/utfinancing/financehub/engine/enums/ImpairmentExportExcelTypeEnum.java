package com.utfinancing.financehub.engine.enums;

/**
 * 减值计提导出excel类型
 */
public enum ImpairmentExportExcelTypeEnum {
//    EXCEL_TYPE_1("export_excel_1", "附件1：其他应收款_诉讼费保全费"),
    // EXCEL_TYPE_2("export_excel_2", "附件2：其他应收款项"),
    // EXCEL_TYPE_3("export_excel_3", "附件3：应收投资性房地产"),
//    EXCEL_TYPE_2("export_excel_2", "附件2：恒信应收蓬莱租赁清单"),
    EXCEL_TYPE_3("export_excel_3", "附件3：债务重组项目长期应收款"),

    ;
    private final String code;
    private final String desc;

    ImpairmentExportExcelTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (ImpairmentExportExcelTypeEnum myEnum : ImpairmentExportExcelTypeEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }
}
