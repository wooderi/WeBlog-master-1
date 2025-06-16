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
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2023-05-11 9:02
 * @description: TODO
 **/
@Component
@Slf4j
public class MinioUtil {

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000; // 1秒

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    /**
     * 确保存储桶存在，如果不存在则创建
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

    public String uploadFile(MultipartFile file) throws Exception {
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("文件大小不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        String key = UUID.randomUUID().toString().replace("-", "");
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        String objectName = String.format("%s%s", key, suffix);

        // 添加重试逻辑
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

                // 直接使用publicEndpoint生成URL，而不是endpoint
                String url = String.format("%s/%s/%s",
                        minioProperties.getPublicEndpointOrDefault(),
                        minioProperties.getBucketName(),
                        objectName);

                log.info("==> 上传文件至 Minio 成功，访问路径: {}", url);
                return url;

            } catch (SocketException | SocketTimeoutException e) {
                // 这些网络连接相关的异常可以重试（包括ConnectException，因为它是SocketException的子类）
                lastException = e;
                log.warn("==> 上传文件至 Minio 失败 (尝试 {}/{}): 网络连接问题: {}",
                        attempt, MAX_RETRY_ATTEMPTS, e.getMessage());

                if (attempt < MAX_RETRY_ATTEMPTS) {
                    // 延迟一段时间后重试
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt); // 逐次增加等待时间
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("上传被中断", ie);
                    }
                }
            } catch (Exception e) {
                // 记录详细的错误信息
                log.error("==> 上传文件至 Minio 失败：{}, 错误类型: {}", e.getMessage(), e.getClass().getName(), e);

                // 提供更具体的错误信息
                if (e instanceof ErrorResponseException) {
                    ErrorResponseException ere = (ErrorResponseException) e;
                    log.error("==> Minio错误响应: {}, 状态码: {}", ere.getMessage(), ere.response().code());
                    throw new RuntimeException("Minio服务返回错误: " + ere.getMessage(), e);
                } else {
                    throw e;
                }
            }
        }

        // 如果重试后仍然失败
        log.error("==> 上传文件至 Minio 失败：重试 {} 次后仍然失败", MAX_RETRY_ATTEMPTS);
        throw lastException != null ? lastException : new RuntimeException("上传文件失败，请稍后重试");
    }
}
