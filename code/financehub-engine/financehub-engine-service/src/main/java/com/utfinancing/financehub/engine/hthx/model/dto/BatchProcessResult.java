package com.utfinancing.financehub.engine.hthx.model.dto;

import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionDetailEntity;
import lombok.Data;

import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/25 22:04
 */
@Data
public   class BatchProcessResult<T> {
    private final T data;         // 泛型数据结果
    private final Throwable exception; // 异常信息

    public BatchProcessResult(T data, Throwable exception) {
        this.data = data;
        this.exception = exception;
    }

    public T getData() {
        return data;
    }

    public Throwable getException() {
        return exception;
    }
}
