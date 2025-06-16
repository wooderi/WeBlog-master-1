package com.quanxiaoha.weblog.admin.utils;

import com.quanxiaoha.weblog.admin.config.MinioProperties;
import com.quanxiaoha.weblog.common.exception.BizException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


/**
 * MinIO对象存储工具类
 * 负责处理文件上传、存储桶管理等与MinIO相关的操作
 * 集成了重试机制和详细的错误处理
 */
@Component
@Slf4j
public class MinioUtil {

    /**
     * 最大重试次数 - 上传文件时的网络异常重试上限
     */
    private static final int MAX_RETRY_ATTEMPTS = 3;
    /**
     * 重试延迟时间(毫秒) - 基础延迟时间，每次重试会递增
     */
    private static final long RETRY_DELAY_MS = 1000; // 1秒

    /**
     * MinIO配置属性 - 从配置文件注入，包含端点、桶名等信息
     */
    @Autowired
    private MinioProperties minioProperties;

    /**
     * MinIO客户端 - 用于执行具体的MinIO操作
     */
    @Autowired
    private MinioClient minioClient;

    /**
     * 确保存储桶存在，如果不存在则创建
     *
     * @param bucketName 存储桶名称
     * @throws Exception 当检查或创建存储桶失败时抛出
     */
    public void ensureBucketExists(String bucketName) throws Exception {
        try {
            log.info("==> 检查存储桶是否存在: {}, 使用端点: {}", bucketName, minioProperties.getEndpoint());
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!bucketExists) {
                log.info("==> 存储桶 {} 不存在，开始创建...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("==> 存储桶 {} 创建成功", bucketName);
            } else {
                log.info("==> 存储桶 {} 已经存在", bucketName);
            }
        } catch (Exception e) {
            log.error("==> 检查或创建存储桶失败: {}", e.getMessage(), e);
            if (e instanceof ConnectException) {
                throw new RuntimeException("连接Minio服务器失败，请检查Minio服务是否启动以及端口配置是否正确", e);
            } else if (e instanceof ErrorResponseException) {
                throw new RuntimeException("Minio服务返回错误，可能是端点配置错误或权限问题: " + e.getMessage(), e);
            } else {
                throw e;
            }
        }
    }

    /**
     * 上传文件到MinIO存储
     * 包含文件验证、重命名、重试机制和错误处理
     *
     * @param file 要上传的MultipartFile对象
     * @return 上传成功后的文件访问URL
     * @throws Exception 当上传过程中发生错误时抛出
     */
    public String uploadFile(MultipartFile file) throws Exception {
        // 验证文件是否为空
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("文件大小不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        // 生成唯一文件名 - 使用UUID确保文件名唯一性，避免冲突
        String key = UUID.randomUUID().toString().replace("-", "");
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String objectName = String.format("%s%s", key, suffix);

        // 添加重试逻辑 - 针对网络不稳定情况进行多次尝试
        Exception lastException = null;
        for (int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                log.info("==> 尝试第 {} 次上传文件至 Minio, ObjectName: {}, Endpoint: {}",
                        attempt, objectName, minioProperties.getEndpoint());

                // 确保存储桶存在
                ensureBucketExists(minioProperties.getBucketName());

                log.info("==> 开始执行文件上传操作...");
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(contentType)
                        .build());

                // 构建文件访问URL - 使用publicEndpoint确保生成可公开访问的URL
                String url = String.format("%s/%s/%s",
                        minioProperties.getPublicEndpointOrDefault(),
                        minioProperties.getBucketName(),
                        objectName);

                log.info("==> 上传文件至 Minio 成功，访问路径: {}", url);
                return url;

            } catch (SocketException | SocketTimeoutException e) {
                // 处理网络连接相关异常 - 这些异常适合进行重试
                lastException = e;
                log.warn("==> 上传文件至 Minio 失败 (尝试 {}/{}): 网络连接问题: {}",
                        attempt, MAX_RETRY_ATTEMPTS, e.getMessage());

                if (attempt < MAX_RETRY_ATTEMPTS) {
                    // 指数退避策略 - 每次重试等待时间递增
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("上传被中断", ie);
                    }
                }
            } catch (Exception e) {
                // 处理其他类型异常 - 通常是不可重试的错误
                log.error("==> 上传文件至 Minio 失败：{}, 错误类型: {}", e.getMessage(), e.getClass().getName(), e);

                // 处理MinIO服务返回的错误响应
                if (e instanceof ErrorResponseException) {
                    ErrorResponseException ere = (ErrorResponseException) e;
                    log.error("==> Minio错误响应: {}, 状态码: {}", ere.getMessage(), ere.response().code());
                    throw new RuntimeException("Minio服务返回错误: " + ere.getMessage(), e);
                } else {
                    throw e;
                }
            }
        }

        // 所有重试均失败时抛出异常
        log.error("==> 上传文件至 Minio 失败：重试 {} 次后仍然失败", MAX_RETRY_ATTEMPTS);
        throw lastException != null ? lastException : new RuntimeException("上传文件失败，请稍后重试");
    }
}
