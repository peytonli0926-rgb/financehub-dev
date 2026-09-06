package com.utfinancing.financehub.engine.hthx.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 应用模块名称: 动态批次大小调整工具类
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/23 13:46
 */
public class HthxDynamicBatchSizeUtils {

    /**
     * @description: 基础配置参数（可改为从配置中心读取）
     * @author: zhangli.chen
     **/
    // 基准批次大小
    private static final int BASE_BATCH_SIZE = 1000;
    // 最小批次大小
    private static final int MIN_BATCH_SIZE = 50;
    // 最大批次大小
    private static final int MAX_BATCH_SIZE = 5000;
    // CPU权重系数
    private static final double CPU_WEIGHT = 0.4;
    // 内存权重系数
    private static final double MEM_WEIGHT = 0.3;
    // 队列权重系数
    private static final double QUEUE_WEIGHT = 0.3;

    /**
     * 计算最优批次大小
     * @param executor 关联的线程池（可为null）
     * @return 动态计算后的批次大小
     */
    public static int calculateOptimalBatchSize(ThreadPoolExecutor executor) {
        // 获取系统指标
        double cpuLoad = getCpuLoad();
        double memUsage = getMemoryUsage();
        double queuePressure = getQueuePressure(executor);

        // 计算压力系数（0~1, 值越大系统压力越大）
        double pressure =
                cpuLoad * CPU_WEIGHT +
                        memUsage * MEM_WEIGHT +
                        queuePressure * QUEUE_WEIGHT;
        // 动态调整公式（压力越大批次越小）
        int dynamicSize = (int) (BASE_BATCH_SIZE * (1 - Math.min(pressure, 0.8)));
        // 边界约束
        return Math.min(MAX_BATCH_SIZE, Math.max(MIN_BATCH_SIZE, dynamicSize));
    }

    /**
     * 系统CPU指标获取
     */
    private static double getCpuLoad() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                // 获取系统 CPU 使用率
                double systemCpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getSystemCpuLoad();
                // 获取当前进程 CPU 使用率
                //double processCpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad();
                return systemCpuLoad;
            }
            return osBean.getSystemLoadAverage() / Runtime.getRuntime().availableProcessors();
        } catch (Exception e) {
            // 降级默认值
            return 0.5; 
        }
    }

    /**
     * 系统内存指标获取
     */
    private static double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        return (double) used / max;
    }

    /**
     * 系统执行队列获取
     */
    private static double getQueuePressure(ThreadPoolExecutor executor) {
        if (executor == null) return 0;
        int queueSize = executor.getQueue().size();
        int remainingCapacity = executor.getQueue().remainingCapacity();
        int totalCapacity = queueSize + remainingCapacity;
        return totalCapacity == 0 ? 0 : (double) queueSize / totalCapacity;
    }

}
