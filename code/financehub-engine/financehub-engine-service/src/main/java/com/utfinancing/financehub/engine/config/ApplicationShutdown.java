package com.utfinancing.financehub.engine.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {

    @Resource
    private XxlJobSpringExecutor xxlJobSpringExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("getDisplayName:{}", event.getApplicationContext().getDisplayName());
        xxlJobSpringExecutor.destroy();

    }
}
