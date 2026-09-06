package com.utfinancing.financehub.engine.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.config.TaskExecutePoolConfig</li>
 * <li>CreateTime : 2023/12/10 11:22</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class TaskExecutePoolConfig {

    @Value("${task.pool.corePoolSize:10}")
    private int corePoolSize; //线程池大小

    @Value("${task.pool.maxPoolSize:20}")
    private int maxPoolSize;//最大线程数

    @Value("${task.poolqueueCapacity:1000}")
    private int queueCapacity; //队列缓存

    @Value("${task.pool.keepAliveSeconds:60}")
    private int keepAliveSeconds; //保持活跃时间

    @Bean("asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int i = Runtime.getRuntime().availableProcessors();
        if (ObjectUtil.isNull(corePoolSize)) {
            corePoolSize = i * 2;
        }
        if (ObjectUtil.isNull(maxPoolSize)) {
            maxPoolSize = i * 2;
        }
        if (ObjectUtil.isNull(queueCapacity)) {
            queueCapacity = i * 20;
        }
        if (ObjectUtil.isNull(keepAliveSeconds)) {
            keepAliveSeconds = 60;
        }
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("financehub-engine-service-async-task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(runnable -> TtlRunnable.get(runnable, true));
        executor.initialize();
        return executor;
    }

}
