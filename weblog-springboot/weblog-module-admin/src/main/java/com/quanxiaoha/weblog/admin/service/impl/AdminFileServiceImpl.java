package com.quanxiaoha.weblog.admin.service.impl;

import com.quanxiaoha.weblog.admin.model.vo.file.UploadFileRspVO;
import com.quanxiaoha.weblog.admin.service.AdminFileService;
import com.quanxiaoha.weblog.admin.utils.MinioUtil;
import com.quanxiaoha.weblog.common.Response;
// import com.quanxiaoha.weblog.common.enums.ResponseCodeEnum;
import com.quanxiaoha.weblog.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
/**
 * 文件管理服务实现类
 * 负责文件上传及MinIO URL转换操作
 */
public class AdminFileServiceImpl implements AdminFileService {

    @Autowired
    private MinioUtil minioUtil;

    @Value("${minio.endpoint}")
    private String apiEndpoint;

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint;

    // 静态定义错误代码和消息，避免依赖ResponseCodeEnum
    private static final String UPLOAD_ERROR_CODE = "10007";
    private static final String UPLOAD_ERROR_MESSAGE = "文件上传失败";

    /**
     * 上传文件到MinIO存储并返回转换后的公共访问URL
     * @param file 待上传的文件对象
     * @return 包含转换后文件访问URL的响应对象
     */
    @Override
    public Response uploadFile(MultipartFile file) {
        try {
            String url = minioUtil.uploadFile(file);
            // 直接实现URL转换逻辑
            String convertedUrl = convertUrl(url);
            return Response.success(UploadFileRspVO.builder().url(convertedUrl).build());
        } catch (Exception e) {
            log.error("==> 上传文件异常: ", e);
            // 直接使用Response.fail方法，避免使用BizException和ResponseCodeEnum
            return Response.fail(UPLOAD_ERROR_CODE, UPLOAD_ERROR_MESSAGE);
        }
    }

    /**
     * 转换Minio URL
     * 将API端点URL转换为公共访问端点URL
     * 
     * @param url 原始URL
     * @return 转换后的URL
     */
    private String convertUrl(String url) {
        if (url == null || url.isEmpty()) {
            log.warn("转换URL失败: URL为空");
            return url;
        }

        log.info("转换前的URL: {}", url);

        // 如果URL包含API端点，则替换为公共访问端点
        if (url.contains(apiEndpoint)) {
            String convertedUrl = url.replace(apiEndpoint, publicEndpoint);
            log.info("转换Minio URL: {} -> {}", url, convertedUrl);
            return convertedUrl;
        }

        // 如果URL不包含协议，添加公共端点
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            String convertedUrl = publicEndpoint + (url.startsWith("/") ? url : "/" + url);
            log.info("为相对路径添加公共端点: {} -> {}", url, convertedUrl);
            return convertedUrl;
        }

        log.info("URL无需转换: {}", url);
        return url;
    }
}
