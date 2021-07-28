package com.yan.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 的全局配置
 * EnableGlobalMethodSecurity开启Controller的Secured注解
 *
 * @author : 鄢云峰
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 设置security的认证方式，即AuthenticationManager的构建者
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        auth.inMemoryAuthentication()
                .withUser("user").password(bCryptPasswordEncoder().encode("123456")).authorities("ROLE_ADMIN")
                .and().withUser("es").password(bCryptPasswordEncoder().encode("123456")).authorities("ROLE_ES")
                .and().withUser("mysql").password(bCryptPasswordEncoder().encode("123456")).authorities("ROLE_MYSQL")
                .and().withUser("presto").password(bCryptPasswordEncoder().encode("123456")).authorities("ROLE_PRESTO");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                防止csrf攻击
                .csrf()
                .disable()
                //设置为无状态，通常前后端分离的接口都为无状态
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //开启认证
                .authorizeRequests()
                //其他所有的请求都应是通过验证的
                .anyRequest().authenticated()
                .and()
                //这里使用/yan/token做为获取token的路径
                .addFilterBefore(new JwtLoginFilter("/yan/token", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //如下为token的验证的过滤器
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
