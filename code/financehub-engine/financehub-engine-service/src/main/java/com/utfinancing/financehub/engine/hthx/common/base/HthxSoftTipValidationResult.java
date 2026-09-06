package com.utfinancing.financehub.engine.hthx.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用模块名称: 软提示校验结果类
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HthxSoftTipValidationResult {


    /**
     * @description: 是否存在警告
     **/
    private boolean hasWarn;

    /**
     * @description: 软提示文案
     **/
    private String warnMessage;

}
