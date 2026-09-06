package com.utfinancing.financehub.etl.enums;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.enums.ExecutionTaskSystemEnum</li>
 * <li>CreateTime : 2024/04/07 10:17</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum ExecutionTaskSystemEnum {

    KPRL_DATA("KPRL_DATA", "读取开票认领数据任务"),
    KPRL_VOUCHER("KPRL_VOUCHER", "开票认领生成凭证任务"),
    MIDDLE_VOUCEHR_EAS2("MIDDLE_VOUCEHR_EAS2","金蝶中间库同步凭证到EAS2系统任务"),

    FINHUB_VOUCEHR_EAS2("FINHUB_VOUCEHR_EAS2","中台同步凭证(暂存，过账，复核)到EAS2系统任务"),

    FINHUB_VOUCEHR_EAS2_SUBMIT("FINHUB_VOUCEHR_EAS2_SUBMIT","中台同步凭证(提交)到EAS2系统任务");
    ;
    private final String code;
    private final String desc;

    ExecutionTaskSystemEnum(String code, String desc) {
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
