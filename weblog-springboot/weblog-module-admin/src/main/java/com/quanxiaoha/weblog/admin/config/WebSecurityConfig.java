package com.quanxiaoha.weblog.admin.config;

import com.quanxiaoha.weblog.jwt.JwtAuthenticationSecurityConfig;
import com.quanxiaoha.weblog.jwt.RestAccessDeniedHandler;
import com.quanxiaoha.weblog.jwt.RestAuthenticationEntryPoint;
import com.quanxiaoha.weblog.jwt.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Web安全配置类，负责配置应用的安全规则、认证授权机制和资源访问控制
 * 继承WebSecurityConfigurerAdapter以自定义Spring Security配置
 */
@Configurable
@EnableWebSecurity // 启用Web安全功能
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 启用方法级安全注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** 自定义认证入口点，处理未认证请求 */
    @Autowired
    private RestAuthenticationEntryPoint authEntryPoint;
    /** 自定义访问拒绝处理器，处理权限不足请求 */
    @Autowired
    private RestAccessDeniedHandler deniedHandler;

    /** JWT认证安全配置 */
    @Autowired
    private JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    /**
     * 配置HTTP安全规则
     * @param http HttpSecurity对象，用于构建安全配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable() // 禁用表单登录
                .apply(jwtAuthenticationSecurityConfig) // 应用JWT认证配置
            .and()
                // .addFilterAt(jwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .csrf().disable() // 禁用CSRF保护，适用于API接口
            .authorizeRequests() // 配置请求授权规则
                .mvcMatchers("/admin/**").authenticated() // 管理员接口需要认证
                .anyRequest().permitAll() // 其他请求允许匿名访问
            .and()
                .httpBasic().authenticationEntryPoint(authEntryPoint) // 配置HTTP基本认证入口点
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无需创建会话，使用JWT令牌认证
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(deniedHandler) // 配置访问拒绝处理器
            .and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 添加JWT认证过滤器
                ;
    }

    /**
     * 创建Token认证过滤器Bean
     * 用于拦截请求并验证JWT令牌
     * @return TokenAuthenticationFilter实例
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    /**
     * 暴露AuthenticationManager Bean
     * 用于处理认证请求
     * @return AuthenticationManager实例
     * @throws Exception 获取认证管理器过程中可能抛出的异常
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
