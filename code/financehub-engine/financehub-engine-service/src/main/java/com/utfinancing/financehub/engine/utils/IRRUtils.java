package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.List;

public class IRRUtils {
    private static final double EPSILON = 0.00001;
    private static final int MAX_ITERATIONS = 1000;

    public static double calculateIRRYear(List<Double> cashFlows) {
        return calculateIRRYear(cashFlows.toArray(new Double[0]));
    }

    public static double calculateIRRYear(Double[] cashFlows) {
        double irr = irr(cashFlows, -0.1D);
        if (!Double.isNaN(irr)) {
            irr = irr * 12;
        }
        return irr;
    }

    public static double calculateIRR(Double[] cashFlows) {
        double irr = 0.1; // 初始假设IRR值
        double npv;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            npv = calculateNPV(cashFlows, irr);

            if (Math.abs(npv) < EPSILON) {
                return irr;
            }

            double derivative = calculateDerivative(cashFlows, irr);
            irr = NumberUtil.sub(irr, NumberUtil.div(npv, derivative));
        }

        return Double.NaN;

//        throw new RuntimeException("IRR calculation did not converge");
    }

    private static double calculateNPV(Double[] cashFlows, double rate) {
        double npv = 0.0;

        for (int i = 0; i < cashFlows.length; i++) {
            npv += cashFlows[i] / Math.pow(1 + rate, i);
        }

        return npv;
    }

    private static double calculateDerivative(Double[] cashFlows, double rate) {
        double derivative = 0.0;

        for (int i = 0; i < cashFlows.length; i++) {
            derivative -= (i * cashFlows[i]) / Math.pow(1 + rate, i + 1D);
        }

        return derivative;
    }

    public static void main(String[] args) {
//        Double[] cashFlows = {-65835203.08, 0.00, 0.00, 0.00, 0.00, 0.00, 4205309.73, 0.00, 0.00, 4205309.73, 0.00, 0,
//                4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0, 4196363.72, 0, 0,
//                4196452.21};
//IRR*12: 0.15812325953709322
        Double[] cashFlows = {-5803574.87, 100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                100000.00,
                120000.00,
                120000.00,
                2320000.00};
//        double irr = calculateIRR(cashFlows);
//        System.out.println("IRR: " + irr);
//        System.out.println("IRR*12: " + irr * 12);
//
//        System.out.println(getIrr(Arrays.asList(cashFlows)));
        System.out.println(calculateIRRYear(cashFlows));
    }

    public static double irr(Double[] values, double guess) {
        int maxIterationCount = 50;
        double absoluteAccuracy = 1.0E-007D;

        double x0 = guess;

        int i = 0;
        while (i < maxIterationCount) {
            double fValue = 0.0D;
            double fDerivative = 0.0D;
            for (int k = 0; k < values.length; k++) {
                fValue += values[k] / Math.pow(1.0D + x0, k);
                fDerivative += -k * values[k] / Math.pow(1.0D + x0, k + 1);
            }
            double x1 = x0 - fValue / fDerivative;
            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }
            x0 = x1;
            i++;
        }
        return (0.0D / 0.0D);
    }
}