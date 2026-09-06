package com.utfinancing.financehub.engine.enums;

import io.swagger.annotations.ApiModel;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CostMainCategoryExpenseTypeEnum</li>
 * <li>CreateTime : 2023/12/20 10:32</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("成本类-费用大类")
public enum CostMainCategoryExpenseTypeEnum {
    //1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费
    GPS("1","GPS"),
    GRACELETE("2","手环设备款"),
    SERVICE("3","经销商服务费、外部渠道费、海通渠道费"),
    COLLECT_FEE("4","收车费、抵押费、解抵押费"),
    ;
    private final String code;
    private final String desc;

    CostMainCategoryExpenseTypeEnum(String code, String desc) {
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
