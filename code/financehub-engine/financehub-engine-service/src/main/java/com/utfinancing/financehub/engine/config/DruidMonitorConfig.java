package com.utfinancing.financehub.engine.config;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DruidMonitorConfig {
    /**
     * Druid数据库连接池配置文件
     */
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String dbUrl;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String username;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.dynamic.datasource.master.druid.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.dynamic.datasource.master.druid.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.dynamic.datasource.master.druid.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.dynamic.datasource.master.druid.maxWait}")
    private int maxWait;
    @Value("${spring.datasource.dynamic.datasource.master.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource.dynamic.datasource.master.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.dynamic.datasource.master.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.dynamic.datasource.master.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
//    @Value("${spring.datasource.druid.max-evictable-idle-time-millis}")
//    private int maxEvictableIdleTimeMillis;
    @Value("${spring.datasource.dynamic.datasource.master.druid.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.dynamic.datasource.master.druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.dynamic.datasource.master.druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.dynamic.datasource.master.druid.testOnReturn}")
    private boolean testOnReturn;
    @Value("${spring.datasource.dynamic.datasource.master.druid.filters}")
    private String filters;

    @Value("${spring.datasource.dynamic.datasource.master.druid.connect-timeout}")
    private int connectTimeout;

    @Value("${spring.datasource.dynamic.datasource.master.druid.socket-timeout}")
    private int socketTimeout;

    @Value("{spring.datasource.dynamic.druid.connectionProperties}")
    private String connectionProperties;
    @Value("${spring.datasource.druid.stat-view-servlet.loginUsername}")
    private String loginUsername;
    @Value("${spring.datasource.druid.stat-view-servlet.loginPassword}")
    private String loginPassword;
    @Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String resetEnable;

    /**
     * Druid 连接池配置
     */
    @Bean     //声明其为Bean实例
    public DruidDataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setConnectTimeout(connectTimeout);
        datasource.setSocketTimeout(socketTimeout);
        try {
            datasource.setFilters(filters);
        } catch (Exception e) {

        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    /**
     * JDBC操作配置
     */
    @Bean(name = "dataOneTemplate")
    public JdbcTemplate jdbcTemplate(@Autowired DruidDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 配置 Druid 监控界面
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean srb =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //设置控制台管理用户
        srb.addInitParameter("loginUsername", loginUsername);
        srb.addInitParameter("loginPassword", loginPassword);
        //是否可以重置数据
        srb.addInitParameter("resetEnable", resetEnable);
        return srb;
    }

    @Bean
    public FilterRegistrationBean statFilter() {
        //创建过滤器
        FilterRegistrationBean frb =
                new FilterRegistrationBean(new WebStatFilter());
        //设置过滤器过滤路径
        frb.addUrlPatterns("/*");
        //忽略过滤的形式
        frb.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return frb;
    }

}
