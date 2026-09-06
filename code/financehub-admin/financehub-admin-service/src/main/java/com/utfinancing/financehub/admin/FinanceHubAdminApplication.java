package com.utfinancing.financehub.admin;

import com.utfinancing.financehub.common.security.annotation.EnableCustomConfig;
import com.utfinancing.financehub.common.security.annotation.EnableFeignClients;
import com.utfinancing.financehub.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 */
@EnableCustomSwagger2
@EnableCustomConfig
@EnableFeignClients
@SpringBootApplication
public class FinanceHubAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceHubAdminApplication.class, args);
    }

}
