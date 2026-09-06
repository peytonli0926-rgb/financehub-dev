package com.utfinancing.financehub.engine.hthx.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/25 22:07
 */
@Data
public   class ProcessSummary {
    private final int successCount;
    private final int failedCount;
    private final List<String> errorMessages;

    public ProcessSummary(int successCount, int failedCount, List<String> errorMessages) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.errorMessages = errorMessages;
    }
}
