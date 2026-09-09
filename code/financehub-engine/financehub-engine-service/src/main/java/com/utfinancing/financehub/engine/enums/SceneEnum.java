package com.utfinancing.financehub.engine.enums;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 场景常量
 */
public enum SceneEnum {
    BZJCFL("BZJCFL", "保证金重分类"),
    BZJJT("BZJJT", "保证金计提"),
    HZHX("HZHX", "坏账核销"),
    HTQZ("HTQZ", "合同起租"),
    FWFJT("FWFJT", "服务费计提"),
    SYJT("SYJT", "收益计提"),
    YCSXFFT("YCSXFFT", "邮储手续费分摊"),
    JYJGBG("JYJGBG", "交易结构变更"),
    CYC_REFUND("CYC_REFUND", "退款"),
    CYC_SUBSIDY_CONFIRM("CYC_SUBSIDY_CONFIRM", "贴息确认"),
    CYC_OVERDUE("CYC_OVERDUE", "逾期"),
    YFBXF("YFBXF", "应付保险费"),
    BXTB("BXTB", "投保"),
    SSF("SSF","诉讼费"),
    RKCZ("RKCZ","入库处置"),
    RKSH("RKSH","入库赎回"),
    HTCX("HTCX","合同撤销"),
    CWRK("CWRK","财务入库"),
    CWCK("CWCK","财务出库"),
    ZLSK("ZLSK","租赁收款"), //回笼
    GPS("GPS","GPS"),
    SHSBK("SHSBK","手环设备款"),
    WYLSSK("WYLSSK","网银流水收款"),
    WYLSFK("WYLSFK","网银流水付款"),
    SCF("SCF","收车费"),
    QDF("QDF","经销商服务费/外部渠道费/海通渠道费"),
    SGPZ("SGPZ","手工凭证"),
    SGPZRL("SGPZRL","手工凭证-认领"),
    SGPZCX("SGPZCX","手工凭证-冲销"),
    SGPZTZ("SGPZTZ","手工凭证-修改网银编号"),
    HANDS_ADJUST_BALANCE("HANDS_ADJUST_BALANCE", "手工调整余额"),
    SGPZMGRZRQ("SGPZMGRZRQ","手工凭证-修改入账日期"),
    HTCXSK("HTCXSK","合同撤销收款"),
    ZXFUFQY("ZXFUFQY","咨询服务签约"),
    WCTZ("WCTZ","尾差调整"),
    CBABS("CBABS", "出表CBABS"),
    KJFP("KJFP", "开票"),
    ZXFWFQY("ZXFWFQY","咨询服务费签约"),
    JZJT("JZJT","减值计提"),
    ABSZF("ABSZF", "出表ABS转付"),
    DZZCSR("DZZCSR", "抵债资产收入"),
    DZZCJZ("DZZCJZ", "抵债资产结转"),

    ABSSH("ABSSH", "赎回"),
    CQYSKXG("CQYSKXG", "长期应收款-偿还计划修改"),
    CQYSKSR("CQYSKSR", "长期应收款-收入确认"),

    // 资产转让 - 平价转让
    PJZR("PJZR", "平价转让"),
    PJZRDB("PJZRDB", "平价转让调拨"),
    // 资产转让 - 折价转让
    ZJZR("ZJZR", "折价转让"),
    ZJZRDB("ZJZRDB", "折价转让调拨"),

    DSFZR("DSFZR", "第三方转让"),
    DSFZF("DSFZF", "第三方转付"),
    QTZF("QTZF", "其他转付"),
    QTZR("QTZR", "其他转让"),
    ZRFY("ZRFY", "转让合同费"),

    ZLFK("ZLFK","租赁付款"), //回笼
    SDFP("SDFP","收到发票"),
    TACFL("TACFL","ta重分类"),

    SQBXFFP("SQBXFFP",""),
    ZLHL("ZLHL",""),
    BXFFK("BXFFK","保险费付款"),
    HANDS_UPLOAD_BANK("HANDS_UPLOAD_BANK", "上传线下网银"),
    ;
    private final String code;
    private final String desc;

    SceneEnum(String code, String desc) {
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
     * 根据场景编码获取场景名称
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return "";
        }
        for (SceneEnum sceneEnum : SceneEnum.values()) {
            if (sceneEnum.getCode().equals(code)) {
                return sceneEnum.getDesc();
            }
        }
        return "";
    }
}
