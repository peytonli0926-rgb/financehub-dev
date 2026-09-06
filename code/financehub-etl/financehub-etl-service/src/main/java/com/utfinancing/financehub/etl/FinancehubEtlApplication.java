package com.utfinancing.financehub.etl;

import com.utfinancing.financehub.common.security.annotation.EnableCustomConfig;
import com.utfinancing.financehub.common.security.annotation.EnableFeignClients;
import com.utfinancing.financehub.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableCustomSwagger2
@EnableCustomConfig
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FinancehubEtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancehubEtlApplication.class, args);
    }

}
