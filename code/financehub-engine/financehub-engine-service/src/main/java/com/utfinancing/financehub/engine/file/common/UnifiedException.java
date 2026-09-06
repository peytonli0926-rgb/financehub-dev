package com.utfinancing.financehub.engine.file.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UnifiedException extends Exception {

    /**
     * 成功
     */
    public static final int SUCCESS = CommonConstants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = CommonConstants.FAIL;

    /**
     * 错误码
     */
    private int code;

    //错误信息
    private String msg;

    //返回数据
    private Map<Object, Object> data;

    public UnifiedException() {
    }

    public UnifiedException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public UnifiedException(int code, String msg, Map<Object, Object> data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}