package com.utfinancing.financehub.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 服务费凭证状态枚举
 *
 * @author le
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ServiceFeeVoucherStatusEnum {

    NOT_GENERATED("0", "未生成"),
    GENERATED("1", "已生成"),
    REVERSED("2", "已冲销"),
    ;
    private final String code;
    private final String desc;

}
