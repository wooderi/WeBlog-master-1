package com.quanxiaoha.weblog.admin.controller;

import com.quanxiaoha.weblog.admin.service.AdminFileService;
import com.quanxiaoha.weblog.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件管理控制器
 * 处理后台文件上传相关操作
 */
@RestController
@RequestMapping("/admin/file")
@Slf4j
public class AdminFileController {

    @Autowired
    private AdminFileService fileService;

    /**
     * 文件上传接口
     * @param file 要上传的文件
     * @return 上传结果，包含文件访问URL等信息
     */
    @PostMapping("/file/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response uploadFile(@RequestParam MultipartFile file) {
        return fileService.uploadFile(file);
    }
}
