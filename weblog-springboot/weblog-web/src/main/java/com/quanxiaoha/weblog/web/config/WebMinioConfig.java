package com.quanxiaoha.weblog.web.config;

import io.minio.MinioClient;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

/**
 * Web模块的Minio配置类
 */
@Configuration
@Slf4j
public class WebMinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName:weblog}")
    private String bucketName;

    /**
     * 应用启动时输出Minio配置信息
     */
    @PostConstruct
    public void init() {
        log.info("=================================================================");
        log.info("Minio配置信息:");
        log.info("API端点: {}", endpoint);
        log.info("公共访问端点: {}", publicEndpoint);
        log.info("存储桶: {}", bucketName);
        log.info("=================================================================");
    }

    @Bean(name = "webOkHttpClient")
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 30, TimeUnit.MINUTES))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Bean(name = "webMinioClient")
    public MinioClient minioClient() {
        log.info("初始化WebMinioClient，使用端点: {}", endpoint);
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region("us-east-1") // 显式设置区域
                .httpClient(okHttpClient())
                .build();
    }
}