package com.utfinancing.financehub.engine.enums;

import com.google.common.collect.Lists;
import org.apache.catalina.LifecycleState;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.ClaimOrderExpenseTypeEnum</li>
 * <li>CreateTime : 2023/12/04 10:08</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum ClaimOrderExpenseTypeEnum {
    GPS ("GPS费","GPS费"),
    SHOUHUAN ("手环费","手环费"),
    SHOUCHE ("收车费","收车费"),
    DIYA ("抵押费","抵押费"),
    JIEDIYA ("解抵押费","解抵押费"),
    ;
    private final String code;
    private final String desc;

    ClaimOrderExpenseTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static List<String> getCodeList(){
        List<String> codeList = Lists.newArrayList();
        for (ClaimOrderExpenseTypeEnum expenseTypeEnum : ClaimOrderExpenseTypeEnum.values()) {
            codeList.add(expenseTypeEnum.getCode());
        }
        return codeList;
    }
}
