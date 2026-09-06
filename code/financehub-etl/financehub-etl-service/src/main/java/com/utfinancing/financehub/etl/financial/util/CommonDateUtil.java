package com.utfinancing.financehub.etl.financial.util;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.util.CommonDateUtil</li>
 * <li>CreateTime : 2024/04/07 16:04</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public class CommonDateUtil {

    public static LocalDateTime parseStringToLocalDateTime(String dateString){
        if (StringUtils.isEmpty(dateString)) {
            return LocalDateTime.now();
        }
        return DateUtils.parseLocalDateTime(dateString);
    }
}
