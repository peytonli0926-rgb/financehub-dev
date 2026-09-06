package com.utfinancing.financehub.engine.enums;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.BatchTypeEnum</li>
 * <li>CreateTime : 2023/11/15 14:21</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum BatchTypeEnum {
    HZHX("HZHX", "坏账核销"),
    SSF("SSF", "诉讼费转费用"),
    YFBXF("YFBXF", "应付保险费"),
    XXHT("XXHT", "线下合同"),
    HTXXXG("HTXXXG", "合同信息修改"),
    FWFJT("FWFJT", "服务费计提"),
    SYJT("SYJT", "收益计提"),
    BZJ("BZJ", "保证金"),
    YCSXF("YCSXF", "邮储手续费"),
    SSFZF("SSFZF", "诉讼费支付"),
    WYLSSK("WYLSSK", "网银收付款"),
    WYLSFK("WYLSFK", "网银付款"),
    SCF("SCF", "收车费"),
    QDF("QDF", "经销商服务费/外部渠道费/海通渠道费"),
    GPS("GPS","GPS"),
    SHSBK("SHSBK","手环设备款"),

    SGTZYE("SGTZYE","手工调整余额"),
    SGPZ("SGPZ","手工凭证"),
    SGPZRL("SGPZRL","手工凭证-认领"),
    SGPZCX("SGPZCX","手工凭证-冲销"),
    SGPZTZ("SGPZTZ","手工凭证-修改网银编号"),
    SGPZMGRZRQ("SGPZMGRZRQ","手工凭证-修改入账日期"),
    KJFP("KJFP","开票认领"),
    CHARGEOFF("CHARGEOFF", "Charge Off"),
    WCTZ("WCTZ","尾差调整"),
    CBABS("CBABS", "出表ABS"),

    ABSSH("ABSSH", "赎回"),

    ABSZF("ABSZF", "转付"),
    JZJT("JZJT", "减值计提"),
    DZZCZRDJ("DZZCZRDJ", "抵债资产-转入登记"),
    DZZCCSDJ("DZZCCSDJ", "抵债资产-出售登记"),
    DZZCCZDJ("DZZCCZDJ", "抵债资产-出租登记"),
    DZZCCZDJSRQR("DZZCCZDJSRQR", "抵债资产-出租登记-收入确认"),
    DZZCCZDJSRJZ("DZZCCZDJSRJZ", "抵债资产-出租登记-收入结转"),

    HSSBCWRK("HSSBCWRK", "回收设备-财务入库"),
    HSSBCWCK("HSSBCWCK", "回收设备-财务出库"),

    NBZR("NBZR", "平价转让"),
    NBDB("NBDB", "内部调拨"),
    CQYSK("CQYSK", "长期应收款"),
    CQYSKSRQR("CQYSKSRQR", "长期应收款-收入确认"),
    YJZZS("YJZZS", "应交增值税"),
    TSHT("TSHT", "特殊合同状态"),
    TACFL("TACFL", "ta重分类"),
    TAQTYFK("TAQTYFK", "ta其他应付款"),
    ZXFWF("ZXFWF", "咨询服务费"),

    HYB("HYB","金蝶恒运宝"),

    GAXD("GAXD", "贵安，现代物流"),

    WYHYJE("WYHYJE", "网银恒运金额映射表"),

    XXWYSC("XXWYSC","线下网银上传"),

    ;
    private final String code;
    private final String desc;

    BatchTypeEnum(String code, String desc) {
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
