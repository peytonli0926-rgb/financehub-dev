package com.utfinancing.financehub.common.security.annotation;

import java.lang.annotation.*;

/**
 * 自定义feign注解
 * 添加basePackages路径
 *
 * @author ruoyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.cloud.openfeign.EnableFeignClients
public @interface EnableFeignClients {
    String[] value() default {};

    String[] basePackages() default {"com.utfinancing.financehub"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
