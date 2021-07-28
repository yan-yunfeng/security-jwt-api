package com.yan.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author : 鄢云峰
 */
@Configuration
public class UserRepositoryConfig {

    @Bean(name = "securityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.security")
    public DataSource securityDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "securityJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate() {
        return new JdbcTemplate(securityDataSource());
    }

}
