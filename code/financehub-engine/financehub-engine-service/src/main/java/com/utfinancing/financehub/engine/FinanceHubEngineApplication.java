package com.utfinancing.financehub.engine;

import com.utfinancing.financehub.common.security.annotation.EnableCustomConfig;
import com.utfinancing.financehub.common.security.annotation.EnableFeignClients;
import com.utfinancing.financehub.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 系统模块
 */
@EnableCustomSwagger2
@EnableCustomConfig
@EnableFeignClients
@SpringBootApplication
@EnableAsync
public class FinanceHubEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceHubEngineApplication.class, args);
    }

}
