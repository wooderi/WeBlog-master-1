package com.quanxiaoha.weblog.admin.config;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@ConfigurationProperties(prefix = "minio")
@Component
@Data
@Slf4j
public class MinioProperties {
    private String endpoint;
    private String publicEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @PostConstruct
    public void init() {
        log.info("=================================================================");
        log.info("Admin模块 - Minio配置信息:");
        log.info("API端点: {}", endpoint);
        log.info("公共访问端点: {}", publicEndpoint);
        log.info("存储桶: {}", bucketName);
        log.info("=================================================================");
    }

    /**
     * 获取用于生成公共访问URL的端点
     * 如果没有配置publicEndpoint，则使用endpoint
     */
    public String getPublicEndpointOrDefault() {
        return publicEndpoint != null && !publicEndpoint.isEmpty()
                ? publicEndpoint
                : endpoint;
    }
}
