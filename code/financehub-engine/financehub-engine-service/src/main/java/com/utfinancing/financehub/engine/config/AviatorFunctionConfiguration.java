package com.utfinancing.financehub.engine.config;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.spring.SpringContextFunctionLoader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author : lixin
 * @Date : Create in 18/09/2023
 */
@Configuration
public class AviatorFunctionConfiguration implements ApplicationRunner {

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AviatorEvaluator.addFunctionLoader(new SpringContextFunctionLoader(applicationContext));
    }

}
