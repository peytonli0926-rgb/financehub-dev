package com.utfinancing.financehub.engine.hthx.utils;

import java.util.UUID;

/**
 * 应用模块名称: UUID工具类代码
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 20:36
 */

public class HthxUUIDUtils {

    /**
     * 生成一个随机的 UUID 并返回其字符串表示
     * @return UUID 字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个随机的 UUID 并返回其字符串表示（去掉分隔符）
     * @return 不含分隔符的 UUID 字符串
     */
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
