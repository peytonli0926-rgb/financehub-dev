package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.utfinancing.financehub.common.core.exception.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.utils.CommonDateUtils</li>
 * <li>CreateTime : 2024/01/02 10:35</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public class CommonDateUtils {

    public static LocalDateTime parseDateToLocalDateTime(Date date) {
        if (ObjectUtils.isNull(date)) {
            throw new ServiceException("日期不能为空");
        }
        // 将Date对象转换为Instant对象
        Instant instant = date.toInstant();
        // 通过Instant对象获取LocalDateTime对象
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime;
    }

    public static Date parseLocalDateTimeToDate(LocalDateTime localDateTime) {
        if (ObjectUtils.isNull(localDateTime)) {
            throw new ServiceException("日期不能为空");
        }
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static String parseLocalDateTimeToYearMonth(LocalDateTime localDateTime) {
        if (ObjectUtils.isNull(localDateTime)) {
            throw new ServiceException("日期不能为空");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        return localDateTime.format(formatter);
    }

    public static Date parseDateStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new ServiceException("日期转换失败");
        }
    }

    /**
     * 获取去掉时分秒的当前时间
     * @return
     */
    public static Date currentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            throw new ServiceException("日期转换失败");
        }
    }

    /**
     * 保证金默认日期
     * @return
     */
    public static Date bzjDefaultDate() {
        String date = "2019-12-31";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new ServiceException("日期转换失败");
        }
    }

    public static void main(String[] args) {
//        String date = "2019-12-31";
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//             System.out.println(formatter.parse(date));
//        } catch (ParseException e) {
//            throw new ServiceException("日期转换失败");
//        }
        Date before = DateUtil.parse("2024-12-31").toJdkDate();
        System.out.println(getYearValue(before));
        Date after = DateUtil.parse("2026-01-11").toJdkDate();
        System.out.println(getLastMonth(after));
    }

    public static Date lastMonth(Date startDate) {
        LocalDate localDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate lastMonth = localDate.minusMonths(1);
        return Date.from(lastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isWithinLastFiveDays(Date date) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取当前月的最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        // 回退四天，以确保是最后五天的范围
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTime(date);
        // 获取当前月的最后一天
        lastCalendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        // 比较日期是否在当前月的最后五天内
        return date.after(calendar.getTime()) && date.before(lastCalendar.getTime());
    }

    public static Date nextYear(Date startDate) {
        LocalDate localDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate nextYear = localDate.plusYears(1);
        return Date.from(nextYear.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 是否往年
     * @param startDate
     * @return
     */
    public static Boolean isPreviousYears(Date startDate) {
        Calendar today = Calendar.getInstance();
        Calendar targetDate = Calendar.getInstance();
        targetDate.setTime(startDate);
        return targetDate.get(Calendar.YEAR) < today.get(Calendar.YEAR);

    }

    /**
     * 获取年
     * @param startDate
     * @return
     */
    public static Integer getYearValue(Date startDate) {
        LocalDate localDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate.getYear();
    }

    /**
     * 获取月
     * @param startDate
     * @return
     */
    public static Integer getMonthValue(Date startDate) {
        LocalDate localDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate.getMonthValue(); // 获取月份（1-12）
    }

    /**
     * 获取月
     * @param startDate
     * @return
     */
    public static LocalDate getLastMonth(Date startDate) {
        LocalDate localDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate.minusMonths(1);
    }

}
