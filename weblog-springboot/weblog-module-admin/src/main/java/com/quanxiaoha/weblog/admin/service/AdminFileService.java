package com.quanxiaoha.weblog.admin.service;

import com.quanxiaoha.weblog.common.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理服务接口
 * 负责文件上传及MinIO URL转换操作
 */
public interface AdminFileService {
    /**
     * 上传文件到MinIO存储
     * @param file 待上传的文件
     * @return 包含文件访问URL的响应
     */
    Response uploadFile(MultipartFile file);
}
