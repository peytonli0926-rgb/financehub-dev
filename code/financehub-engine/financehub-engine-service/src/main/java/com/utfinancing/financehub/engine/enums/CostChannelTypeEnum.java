package com.utfinancing.financehub.engine.enums;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CostChannelTypeEnum</li>
 * <li>CreateTime : 2023/12/16 13:30</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum CostChannelTypeEnum {
   //1：经销商服务费,2：外部渠道费,3：海通渠道费,4：收车费,5：抵押费,6：解抵押费,7：安装费，8：服务费,9:设备款）
    SERVER_FEE ("1", "经销商服务费"),
    EXTERNAL_CHANNEL_FEE ("2", "外部渠道费"),
    HAITONG_CHANNEL_FEE ("3", "海通渠道费"),
    COLLECT_FEE ("4", "收车费"),
    MORTGAGE_FEE ("5", "抵押费"),
    DECOLLATERA_FEE ("6", "解抵押费"),
    INSTALL_FEE ("7", "安装费"),
    SERVICE_FEE ("8", "服务费"),
    EQUIPMENT_FEE ("9", "设备款"),
    ;
    private final String code;
    private final String desc;

    CostChannelTypeEnum(String code, String desc) {
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
     * 根据desc获取code
     * @param desc
     * @return
     */
    public static String getCodeByDesc(String desc){
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        for (CostChannelTypeEnum typeEnum : CostChannelTypeEnum.values()) {
            if (typeEnum.getDesc().equals(desc)) {
                return typeEnum.getCode();
            }
        }
        return null;
    }


    /**
     * 根据Code获取desc
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CostChannelTypeEnum typeEnum : CostChannelTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
