package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CheckTypeEnum</li>
 * <li>CreateTime : 2023/10/12 09:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
//对账记录状态
public enum CheckTargetEnum {
    //Detail:科目明细余额,Kingdee:金蝶科目余额,Middle:金蝶中间表发生额
    DETAIL("Detail", "科目明细余额"),
    KINGDEE("Kingdee", "金蝶科目余额"),

    MIDDLE("Middle", "金蝶中间表发生额"),

    COMMON_XW_TEST("XW-test", "小微系统获取xxx数据"),
    SFBXF("shifbxf", "实付保险费"),

    YSXXS("ysxxs", "应收销项税"),

    WQRSK("wqrsk", "未确认收款"),

    ZXFWF("zxfwf", "咨询服务费"),

    YFSBK("yfsbk", "应付设备款"),

    YSZLK("yszlk", "应收租赁款"),

    YFBZJ("yfbzj", "应付保证金"),

    ;

    private final String code;
    private final String desc;

    CheckTargetEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckTargetEnum enums : CheckTargetEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static CheckTargetEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckTargetEnum enums : CheckTargetEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
