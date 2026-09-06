package com.utfinancing.financehub.engine.hthx.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用模块名称: 软提示字段校验注解
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 19:57
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckField {}