/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utfinancing.financehub.engine.hthx.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.java.Log;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @apiNote: JDK 8  新日期类 格式化与字符串转换 工具类
 */
public class HthxDateUtils {

    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYYMM = "yyyyMM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static String[] parsePatterns = {
            "yyyyMMdd","yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private static String[] parseSimplePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss"};

    public static final DateTimeFormatter DFY_MD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DFY_MD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * LocalDateTime 转时间戳
     *
     * @param localDateTime /
     * @return /
     */
    public static Long getTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timeStamp /
     * @return /
     */
    public static LocalDateTime fromTimeStamp(Long timeStamp) {
        return LocalDateTime.ofEpochSecond(timeStamp, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * LocalDateTime 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDateTime /
     * @return /
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDate /
     * @return /
     */
    public static Date toDate(LocalDate localDate) {
        if(localDate==null){
            return null;
        }
        return toDate(localDate.atTime(LocalTime.now(ZoneId.systemDefault())));
    }


    /**
     * Date转 LocalDateTime
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param date /
     * @return /
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param patten /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, String patten) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        return df.format(localDateTime);
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param df /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter df) {
        return df.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static String localDateTimeFormatyMdHms(LocalDateTime localDateTime) {
        return DFY_MD_HMS.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public String localDateTimeFormatyMd(LocalDateTime localDateTime) {
        return DFY_MD.format(localDateTime);
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormatyMdHms(String localDateTime) {
        return LocalDateTime.from(DFY_MD_HMS.parse(localDateTime));
    }

    /**
     *localDate
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }


    /**
     * @description:
     * @author: zhangli.chen
     * @date 2024/06/06 18:01
     * @param: str
     * @return Date
     **/
    public static Date stringToDate(String str) {
        if(StringUtils.isStrNotEmpty(str)){
            Date date = new Date();
            DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            try {
                date = df.parse(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    public static Date stringToSimpleDate(String str) {
        if(StringUtils.isStrNotEmpty(str)){
            Date date = new Date();
            DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
            try {
                date = df.parse(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    /**
     * 通过定义模式将日期型字符串转化为日期格式
     */
    public static Date parseDateByPatterns(Object str)
    {
        if (str == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(str.toString(), parseSimplePatterns);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 日期型字符串转化为长日期格式
     **/
    public static String dateToStrLong(Date longDate) {
        if (longDate == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return formatter.format(longDate);
        } catch (Exception e)
        {
            return null;
        }
    }


    /**
     * 日期型字符串转化为短日期格式
     **/
    public static String dateToStrShort(Date shortDate) {
        if (shortDate == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);
            return formatter.format(shortDate);
        } catch (Exception e)
        {
            return null;
        }
    }


    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     **/
    public static Date strToLongDate(String strDate)
    {
        if (strDate == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(strDate, YYYY_MM_DD_HH_MM_SS);
        } catch (Exception e)
        {
            return null;
        }
    }


    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     **/
    public static Date strToShortDate(String strDate)
    {
        if (strDate == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(strDate, YYYY_MM_DD);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 日期路径 即年/月/日
     */
    public static String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }


    /**
     * 将日期转换为长字符串日期格式
     */
    public static String dateToStrLongStr(Date shortDate) {
        if (shortDate == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDDHHMMSS);
            return formatter.format(shortDate);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 生成从开始日期到结束日期之间的每一天日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 日期列表
     */
    public static List<LocalDate> generateDates(LocalDate start, LocalDate end) {
        if(start==null || end==null || start.isAfter(end)){
            return null;
        }
        List<LocalDate> dates = new ArrayList<>();
        // 计算天数差
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        for (int i = 0; i <= daysBetween; i++) {
            // 按正序添加日期
            dates.add(start.plusDays(i));
        }
        return dates;
    }


    /**
     * 将日期转换为长字符串日期格式
     */
    public static String dateToShortStr(LocalDate shortDate) {
        if (shortDate == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
            return shortDate.format(formatter);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 将日期转换为长字符串日期格式
     */
    public static LocalDateTime localDateToLocalDateTime(LocalDate shortDate) {
        if (shortDate == null) {
            return null;
        }
        try {
            return shortDate.atStartOfDay();
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 获取指定月份前的第一天和倒数最后一天
     */
    public static String[] calculateDateRange(String yearMonthStr,int month) {
        // 定义日期格式
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(YYYY_MM);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        try {
            // 解析输入参数
            YearMonth inputYearMonth = YearMonth.parse(yearMonthStr, monthFormatter);
            YearMonth currentYearMonth = YearMonth.from(currentDate);
            LocalDate startDate;
            LocalDate endDate;
            // 判断是否是同一个月份
            if (inputYearMonth.equals(currentYearMonth)) {
                // 结束日期 = 当前日期
                endDate = currentDate;
                // 开始日期 = n个自然月前
                startDate = currentDate.minusMonths(month);
            } else {
                // 结束日期 = 输入月份的最后一天
                endDate = inputYearMonth.atEndOfMonth();
                // 开始日期 = 结束日期减去n个自然月
                startDate = endDate.minusMonths(month);
            }
            // 格式化为要求的字符串格式
            return new String[]{
                    startDate.format(dateFormatter),
                    endDate.format(dateFormatter)
            };
        } catch (DateTimeParseException e) {
            DateTime startDate = DateUtil.beginOfMonth(DateUtil.date());
            DateTime endDate = DateUtil.endOfMonth(DateUtil.date());
            return new String[]{DateUtil.format(startDate,YYYY_MM_DD),DateUtil.format(endDate,YYYY_MM_DD)};
        }
    }



}
