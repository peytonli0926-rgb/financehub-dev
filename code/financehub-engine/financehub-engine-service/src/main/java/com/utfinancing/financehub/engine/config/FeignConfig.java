package com.utfinancing.financehub.engine.config;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC; // 或者选择其他日志级别
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .retryer(new Retryer.Default(1000, 3000, 3))
                .options(new Request.Options(3600000, 3600000)); // 设置最大连接数和连接超时时间
    }
}
