package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.org.apache.bcel.internal.generic.LADD;
import com.utfinancing.financehub.common.core.exception.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
public class PeriodCodeUtil {

    private static final String DATA_FORMATER = "yyyyMMdd";

    public static LocalDate parseFirstDayOfMonth(Integer periodCode){
        LocalDate firstDay = LocalDateTimeUtil.parseDate(NumberUtil.toStr(periodCode)+"01", DATA_FORMATER);
        return firstDay;
    }

    public static LocalDate parseLastDayOfMonth(Integer periodCode){
        LocalDate firstDay = LocalDateTimeUtil.parseDate(NumberUtil.toStr(periodCode)+"01", DATA_FORMATER);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(LocalDateTimeUtil.toEpochMilli(firstDay));
        //月份+1，天设置为0。下个月第0天，就是这个月最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        LocalDate lastDay = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        return lastDay;
    }

    public static Integer periodCodeByLocalDateTime(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        YearMonth yearMonth = YearMonth.from(localDateTime);
        return Integer.parseInt(yearMonth.toString().replace("-",""));
    }

    public static Integer periodCodeByDate(Date date) {
        if (null == date) {
            return null;
        }
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        YearMonth yearMonth = YearMonth.from(localDateTime);
        return Integer.parseInt(yearMonth.toString().replace("-",""));
    }

    public static Date LocalDateToDate(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 获取上一年12月会计期间
     */
    public static Integer getLastYearMonthPeriodCode(Integer periodCode){
        int currentYear = YearMonth.parse(StrUtil.toString(periodCode), DateTimeFormatter.ofPattern("yyyyMM")).getYear();
        int lastYear = currentYear - 1;
        return NumberUtil.parseInt(StrUtil.toString(lastYear) + "12");
    }

    /**
     * 获取上月会计期间
     */
    public static Integer getLastMonthPeriodCode(Integer periodCode){
        YearMonth current = YearMonth.parse(StrUtil.toString(periodCode), DateTimeFormatter.ofPattern("yyyyMM"));
        YearMonth last = current.plusMonths(-1);
        return NumberUtil.parseInt(last.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }

    /**
     * 获取会计期间减一个月，天为减一个月的最后一天
     * @param periodCode
     * @return
     */
    public static LocalDate parseLastMonthDayOfMonth(Integer periodCode){
        LocalDate firstDay = LocalDateTimeUtil.parseDate(NumberUtil.toStr(periodCode)+"01", DATA_FORMATER);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(LocalDateTimeUtil.toEpochMilli(firstDay));
        //月份+1，天设置为0。下个月第0天，就是这个月最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        LocalDate lastDay = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return lastDay;
    }

    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 2024);
//        //Calendar的month是从0开始，这里不减1，表示加一个月，即当前打开的会计期间
//        calendar.set(Calendar.MONTH, 12);
//        calendar.set(Calendar.DAY_OF_MONTH, 0);
//        LocalDate lastDay = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
//
//        System.out.println("时间"+lastDay);
       String dateString = "2024-01-25 14:08:48";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(formatter.parse(dateString));
        } catch (ParseException e) {
            throw new ServiceException("日期转换失败");
        }
    }


}
