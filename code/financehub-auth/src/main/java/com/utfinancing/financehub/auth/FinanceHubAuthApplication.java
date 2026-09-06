package com.utfinancing.financehub.auth;

import com.utfinancing.financehub.common.security.annotation.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证授权中心
 *
 * @author ruoyi
 */
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FinanceHubAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceHubAuthApplication.class, args);
    }

}
