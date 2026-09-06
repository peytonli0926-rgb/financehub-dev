package com.utfinancing.financehub.engine.hthx.common.async;


import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/1/1 14:53
 */
@Slf4j
@Configuration
@EnableAsync
@RefreshScope
public class AsyncConfig {

    @Value("${ThreadPool.xxlCoreSize}")
    public int xxlCoreSize;

    @Value("${ThreadPool.xxlMaxSize}")
    public int xxlMaxSize;

    @Value("${ThreadPool.queueCapacity}")
    public int queueCapacity;

    @Value("${ThreadPool.aliveSec}")
    public int aliveSec;

    /**
     * 每个任务分配一个异步线程
     **/
    @Bean("hthxTaskAsyncExecutor")
    public ThreadPoolTaskExecutor hthxTaskAsyncExecutor() {
        //ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public void execute(Runnable task) {
                log.info("hthx-task-提交任务到线程池: {}", task);
                super.execute(task);
            }
        };
        //核心线程池大小
        executor.setCorePoolSize(xxlCoreSize);
        //最大线程数
        executor.setMaxPoolSize(xxlMaxSize);
        //队列容量
        executor.setQueueCapacity(queueCapacity);
        //活跃时间
        executor.setKeepAliveSeconds(aliveSec);
        //线程名字前缀
        executor.setThreadNamePrefix("hthx-task-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        // 拒绝策略-默认的拒绝策略-通过抛出异常来立即通知调用者任务无法被执行-使用这个策略时，调用者需要准备好处理这个异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置线程池在关闭时等待所有任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待的时长和时间单位
        executor.setAwaitTerminationSeconds(120);
        executor.setTaskDecorator(runnable -> TtlRunnable.get(runnable, true));
        executor.initialize();
        return executor;
    }

}
