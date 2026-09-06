package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-11
 * @Description :   Excel-xirr公式 实现工具
 * @Modified :
 */
public class XirrUtils {

    public final static Double MIN_DISTANCE = 1E-15;    // 目标精确度，允许的最小差值（建议精确到8-15位小数）
    public final static Double MIN_VALUE = 1E-8;        // 允许的净现值偏差最小范围
    public final static int MAX_ITERATION = 100;        // 最高迭代次数（防止卡死，通常100次循环足够）
    public final static Double DEFAULT_XIRR_GUESS = 0.1D;        // 默认Xirr猜测值
    public final static Double FULL_YEAR_DAYS = 365.0D;    // 全年天数


    public static Double xirr(List<Double> values, List<Date> dates) {
        return xirr(values.toArray(new Double[0]),
                dates.toArray(new Date[0]), DEFAULT_XIRR_GUESS);
    }


    public static Double xirr(Double[] values, Date[] dates) {
        return xirr( values, dates, DEFAULT_XIRR_GUESS);
    }

    /**
     * 计算净现值为0的收益率<br>
     * 说明:使用迭代折半查近视值法计算函数 XIRR。通过改变收益率(从 guess 开始)，不断修正计算结果，直至其精度小于 1E-7。<br>
     * 如果函数 XIRR 运算 100 次，仍未找到结果，则返回错误值 NaN。
     *
     * @param values 现金流量(必须至少一个正现金流和一个负现金流)
     * @param dates  日期
     * @param guess  猜测值
     * @return 收益率
     */
    public static Double xirr(Double[] values, Date[] dates, double guess) {
        Double result = Double.NaN;            // 返回结果
        Double irrGuess = DEFAULT_XIRR_GUESS;    // 计算xirr猜测值折半量
        Double sumCashFlows = 0.0D;                    // 现金流量和
        boolean wasHi = false;                // 防止遗漏区间标识
        Double npv = 0.0D;                    // 净现值
        int negativeCashFlowCount = 0;                    // 正现金流个数
        int positiveCashFlowCount = 0;                    // 负现金流个数

        if (values == null || values.length == 0) return result;
        if (dates == null || dates.length == 0) return result;
        if (values.length != dates.length) return result;


        for (int i = 0; i < values.length; i++) {
            sumCashFlows += values[i];
            if (values[i] > 0.0) {
                negativeCashFlowCount++;
            } else if (values[i] < 0.0) {
                positiveCashFlowCount++;
            }
        }

        if (negativeCashFlowCount <= 0 || positiveCashFlowCount <= 0) return result;

        if (!Double.isNaN(guess)) {
            irrGuess = guess;
            if (irrGuess <= 0.0) irrGuess = 0.5;
        }

        Double irr = sumCashFlows < 0 ? NumberUtil.mul(irrGuess, Double.valueOf(-1)) : irrGuess;

        for (int i = 0; i <= MAX_ITERATION; i++) {

            npv = getXirrNpvValue(irr, values, dates);

            if (Math.abs(npv) < MIN_VALUE) {
                result = irr;
                break;
            }

            if (npv > 0.0) {
                if (wasHi) irrGuess /= 2;
                irr += irrGuess;
                if (wasHi) {
                    irrGuess -= MIN_DISTANCE;
                    wasHi = false;
                }
            } else {
                irrGuess /= 2;
                irr -= irrGuess;
                wasHi = true;
            }

            if (irrGuess <= MIN_DISTANCE) {
                result = irr;
                break;
            }
        }

        return result;
    }

    /**
     * 根据公式计算净现值
     *
     * @param guess  猜测值
     * @param values 现金流量(必须至少一个正现金流和一个负现金流)
     * @param dates  日期
     * @return 净现值
     */
    public static Double getXirrNpvValue(final double guess, Double[] values, Date[] dates) {
        Double result = 0.0D;
        // 0 = sum(values[i] / (1 + rate)^((dates[i] - dates[1]) / 365))
        for (int i = 0; i < dates.length; i++) {
            result += values[i] / Math.pow(1 + guess, getIntervalDays(dates[i], dates[0]) / FULL_YEAR_DAYS);
        }
        return result;
    }

    private static Long getIntervalDays(Date endDate, Date startDate) {
        long day = 24 * 60 * 60 * 1000L;

        return (endDate.getTime() - startDate.getTime()) / day;
    }

    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        Double[] values = new Double[]{-65835203d,
//                4205309.73d,
//                4205309.73d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196363.72d,
//                4196452.21d};
//
//        Date[] dates = new Date[]{DateUtil.parseDate("2022/12/19"),
//                DateUtil.parseDate("2023/6/19"),
//                DateUtil.parseDate("2023/9/19"),
//                DateUtil.parseDate("2023/12/19"),
//                DateUtil.parseDate("2024/3/19"),
//                DateUtil.parseDate("2024/6/19"),
//                DateUtil.parseDate("2024/9/19"),
//                DateUtil.parseDate("2024/12/19"),
//                DateUtil.parseDate("2025/3/19"),
//                DateUtil.parseDate("2025/6/19"),
//                DateUtil.parseDate("2025/9/19"),
//                DateUtil.parseDate("2025/12/19"),
//                DateUtil.parseDate("2026/3/19"),
//                DateUtil.parseDate("2026/6/19"),
//                DateUtil.parseDate("2026/9/19"),
//                DateUtil.parseDate("2026/12/19"),
//                DateUtil.parseDate("2027/3/19"),
//                DateUtil.parseDate("2027/6/19"),
//                DateUtil.parseDate("2027/9/19"),
//                DateUtil.parseDate("2027/12/19")};
//        Double xirr = XirrUtils.xirr(values, dates);
//        System.out.println(xirr);
//        System.out.println(NumberUtil.formatPercent(xirr,5));
//        long end = System.currentTimeMillis();
//        System.out.println(end-start);

        List<Date> dates = new ArrayList<>();
        Double[] values = new Double[]{
                4205309.73d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196363.72d,
                4196452.21d,
                -1280d};
        List<Double> numbers = new ArrayList<>(Arrays.asList(values));  //64042264.07

        dates.add(DateUtil.parseDate("2023/9/19"));
        dates.add(DateUtil.parseDate("2023/12/19"));
        dates.add(DateUtil.parseDate("2024/3/19"));
        dates.add(DateUtil.parseDate("2024/6/19"));
        dates.add(DateUtil.parseDate("2024/9/19"));
        dates.add(DateUtil.parseDate("2024/12/19"));
        dates.add(DateUtil.parseDate("2025/3/19"));
        dates.add(DateUtil.parseDate("2025/6/19"));
        dates.add(DateUtil.parseDate("2025/9/19"));
        dates.add(DateUtil.parseDate("2025/12/19"));
        dates.add(DateUtil.parseDate("2026/3/19"));
        dates.add(DateUtil.parseDate("2026/6/19"));
        dates.add(DateUtil.parseDate("2026/9/19"));
        dates.add(DateUtil.parseDate("2026/12/19"));
        dates.add(DateUtil.parseDate("2027/3/19"));
        dates.add(DateUtil.parseDate("2027/6/19"));
        dates.add(DateUtil.parseDate("2027/9/19"));
        dates.add(DateUtil.parseDate("2027/12/19"));
        dates.add(DateUtil.parseDate("2023/6/21"));
        BigDecimal v = calXNPV(0.07404885590076d, dates, numbers);
        System.out.println(v);

    }

    public static BigDecimal calXNPV(Double xirrRateDouble, List<Date> changeDates, List<Double> changeCashFlows) {
        Date firstDate = changeDates.get(changeDates.size() - 1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < changeDates.size() - 1; i++) {
            Date date = changeDates.get(i);
            Double amount = changeCashFlows.get(i);
            double betweenDay = DateUtil.betweenDay(firstDate, date, true);
            double pow = Math.pow(NumberUtil.add(BigDecimal.ONE, BigDecimal.valueOf(xirrRateDouble)).doubleValue(), betweenDay / 365);
            BigDecimal div = BigDecimal.valueOf(NumberUtil.div(amount.doubleValue(), pow)).setScale(2, RoundingMode.HALF_UP);
//            System.out.println(div);
            totalAmount = NumberUtil.add(totalAmount, div);
        }
        return totalAmount;
    }


}
