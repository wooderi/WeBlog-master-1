package com.quanxiaoha.weblog.admin.config;

import io.minio.MinioClient;
import lombok.Getter;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


/**
 * @description: Admin模块Minio配置类，负责Minio客户端的初始化和配置校验
 **/
@Configuration
@Slf4j
public class MinioConfig {
    @Autowired
    private MinioProperties minioProperties;

    /**
     * 初始化方法，用于校验Minio配置参数
     */
    @PostConstruct
    public void init() {
        log.info("=================================================================");
        log.info("Minio配置校验 - Admin模块:");
        log.info("Endpoint: {}", minioProperties.getEndpoint());
        log.info("PublicEndpoint: {}", minioProperties.getPublicEndpoint());
        log.info("BucketName: {}", minioProperties.getBucketName());
        log.info("=================================================================");
    }

    /**
     * 配置OkHttp客户端，设置连接池和超时时间
     * @return OkHttpClient实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 30, TimeUnit.MINUTES))
                .connectTimeout(30, TimeUnit.SECONDS) // 增加超时时间
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 创建Minio客户端实例
     * @param okHttpClient OkHttp客户端
     * @return MinioClient实例
     */
    @Bean
    public MinioClient minioClient(OkHttpClient okHttpClient) {
        log.info("==> 初始化Admin模块MinioClient，使用端点: {}", minioProperties.getEndpoint());
        try {
            MinioClient client = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .region("us-east-1") // 显式设置区域
                    .httpClient(okHttpClient)
                    .build();
            log.info("==> MinioClient初始化成功");
            return client;
        } catch (Exception e) {
            log.error("==> MinioClient初始化失败", e);
            throw new RuntimeException("MinioClient初始化失败", e);
        }
    }
}
