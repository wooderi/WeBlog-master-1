package com.quanxiaoha.weblog.admin.config;


import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;


@Component
public class PasswordEncoderConfig {

    /**
     * 定义一个 Spring Bean，返回一个 BCryptPasswordEncoder 实例，用于密码加密。
     * 
     * @return 一个 BCryptPasswordEncoder 实例。
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 主方法，用于测试 BCryptPasswordEncoder 的加密功能。
     * 
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 打印加密后的 "test" 密码
        System.out.println(encoder.encode("test"));
    }
}
