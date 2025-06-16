package com.quanxiaoha.weblog.web.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Minio初始化配置
 * 在应用启动时确保Minio正确配置，包括存储桶和CORS
 */
@Component
@Slf4j
@Order(1) // 优先执行
public class MinioInitializer implements CommandLineRunner {

    @Autowired
    @Qualifier("webMinioClient")
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("开始初始化Minio配置...");
            initializeBucket();
            configureBucketPolicy();
            log.info("Minio配置完成！");
        } catch (Exception e) {
            log.error("Minio初始化失败", e);
        }
    }

    /**
     * 初始化Minio存储桶
     */
    private void initializeBucket() {
        try {
            // 检查存储桶是否存在
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!bucketExists) {
                log.info("存储桶 {} 不存在，开始创建...", bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("存储桶 {} 创建成功", bucketName);
            } else {
                log.info("存储桶 {} 已存在", bucketName);
            }
        } catch (Exception e) {
            log.error("初始化存储桶失败", e);
        }
    }

    /**
     * 配置存储桶访问策略，允许公共读取
     */
    private void configureBucketPolicy() {
        try {
            // 配置一个允许公共读取的策略
            String policy = String.format(
                    "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
                    bucketName);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policy)
                            .build());

            log.info("已配置存储桶 {} 的公共读取权限", bucketName);
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchBucketPolicy")) {
                log.info("存储桶 {} 无现有策略，已应用新策略", bucketName);
            } else {
                log.error("配置存储桶策略失败", e);
            }
        } catch (Exception e) {
            log.error("配置存储桶策略失败", e);
        }
    }
}