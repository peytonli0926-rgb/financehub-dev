package com.utfinancing.financehub.engine.hthx.common.base;

import com.utfinancing.financehub.engine.hthx.common.annotation.CheckField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用模块名称: 软提示基础校验类
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 19:54
 */


public abstract class HthxBaseWarnDTO {

    /**
     * 生成数据一致性校验的hash值（需子类实现字段选择）
     */
    public abstract int generateDataHash();

    /**
     * 获取校验字段集合（反射自动获取带注解字段）
     */
    protected Object[] getCheckFields() throws IllegalAccessException {
        List<Object> fields = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(CheckField.class)) {
                field.setAccessible(true);
                fields.add(field.get(this));
            }
        }
        return fields.toArray();
    }
}
