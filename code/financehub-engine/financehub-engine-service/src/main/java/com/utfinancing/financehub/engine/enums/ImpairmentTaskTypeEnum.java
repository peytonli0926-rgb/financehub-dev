package com.utfinancing.financehub.engine.enums;

/**
 * 减值计提 任务类型
 */
public enum ImpairmentTaskTypeEnum {
    // 任务类型
    TASK_TYPE_1("上传文件", "上传文件"),
    TASK_TYPE_2("生成凭证", "生成凭证"),
    TASK_TYPE_3("提交", "提交"),
    TASK_TYPE_4("汇总凭证推送至金蝶", "汇总凭证推送至金蝶"),
    TASK_TYPE_5("明细凭证推送至金蝶", "明细凭证推送至金蝶"),


    // 任务状态 字典：task_status
    STATUS_1("1", "进行中"),
    STATUS_2("2", "成功"),
    STATUS_3("3", "失败"),

    ;
    private final String code;
    private final String desc;

    ImpairmentTaskTypeEnum(String code, String desc) {
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
        for (ImpairmentTaskTypeEnum myEnum : ImpairmentTaskTypeEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }
}
