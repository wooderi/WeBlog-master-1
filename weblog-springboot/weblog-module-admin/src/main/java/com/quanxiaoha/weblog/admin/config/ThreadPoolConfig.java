package com.quanxiaoha.weblog.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 该类是一个 Spring 配置类，用于配置线程池。
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 定义一个 Spring Bean，用于创建并配置一个线程池任务执行器。
     * 
     * @return 配置好的线程池任务执行器实例。
     */

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数，线程池始终保持的线程数量
        executor.setCorePoolSize(10);
        // 设置最大线程数，当队列满时，线程池可以创建的最大线程数量
        executor.setMaxPoolSize(20);
        // 设置队列容量，用于存储等待执行的任务
        executor.setQueueCapacity(100);
        // 设置线程名前缀，方便在日志中识别线程来源
        executor.setThreadNamePrefix("WeblogThreadPool-");
        executor.initialize();
        return executor;
    }
}
