package com.quanxiaoha.weblog.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minio URL转换工具
 * 处理Minio URL，确保返回前端的URL使用公共访问端点
 */
@Component
@Slf4j
public class MinioUrlConverter {

    @Value("${minio.endpoint}")
    private String apiEndpoint;

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint;

    @Value("${minio.bucketName:weblog}")
    private String bucketName;

    // 是否将localhost替换为实际IP，用于开发环境中前端能正确访问后端提供的URL
    @Value("${minio.replaceLocalhost:false}")
    private boolean replaceLocalhost;

    // 如果使用localhost，替换为此IP（通常为开发机器实际IP）
    @Value("${minio.localhostReplacement:}")
    private String localhostReplacement;

    // 端口匹配正则表达式
    private static final Pattern PORT_PATTERN = Pattern.compile(":(\\d+)");

    /**
     * 转换Minio URL
     * 将API端点URL转换为公共访问端点URL
     * 
     * @param url 原始URL
     * @return 转换后的URL
     */
    public String convert(String url) {
        if (StringUtils.isEmpty(url)) {
            log.debug("URL为空，不进行转换");
            return url;
        }

        log.info("开始转换URL: {}, apiEndpoint={}, publicEndpoint={}", url, apiEndpoint, publicEndpoint);
        String convertedUrl = url;

        try {
            // 处理相对路径
            if (!url.startsWith("http")) {
                convertedUrl = handleRelativePath(url);
                log.info("相对路径转换结果: {} -> {}", url, convertedUrl);
                return handleLocalhostReplacement(convertedUrl);
            }

            // 从URL中提取端口
            int urlPort = extractPort(url);
            int apiPort = extractPort(apiEndpoint);
            int publicPort = extractPort(publicEndpoint);

            log.debug("URL端口分析: URL端口={}, API端口={}, 公共端口={}", urlPort, apiPort, publicPort);

            // 检查是否需要替换端口
            if (urlPort == apiPort && apiPort != publicPort) {
                // 如果URL使用的是API端口，替换为公共端口
                convertedUrl = replacePort(url, apiPort, publicPort);
                log.info("端口替换: {} -> {} ({}->{})", url, convertedUrl, apiPort, publicPort);
            } else if (!url.contains(publicEndpoint) && url.contains(apiEndpoint)) {
                // 整个域名都不同的情况，替换整个端点
                convertedUrl = url.replace(apiEndpoint, publicEndpoint);
                log.info("端点替换: {} -> {}", url, convertedUrl);
            }

            // 查找特定端口并替换（针对9005->9000的固定转换）
            if (url.contains(":9005/")) {
                convertedUrl = url.replace(":9005/", ":9000/");
                log.info("特定端口替换(9005->9000): {} -> {}", url, convertedUrl);
            }
        } catch (Exception e) {
            log.error("URL转换发生错误: {}", e.getMessage(), e);
            // 出错时返回原始URL
            return url;
        }

        // 如果没有变化，检查是否是因为端点配置问题
        if (convertedUrl.equals(url)) {
            log.debug("URL未发生变化，可能是因为端点配置一致或URL不需要转换");
        }

        return handleLocalhostReplacement(convertedUrl);
    }

    /**
     * 从URL中提取端口号
     */
    private int extractPort(String urlStr) {
        try {
            if (urlStr.startsWith("http")) {
                URL url = new URL(urlStr);
                return url.getPort() != -1 ? url.getPort() : url.getDefaultPort();
            } else {
                // 尝试用正则表达式提取端口
                Matcher matcher = PORT_PATTERN.matcher(urlStr);
                if (matcher.find()) {
                    return Integer.parseInt(matcher.group(1));
                }
            }
        } catch (MalformedURLException e) {
            log.debug("提取端口失败: {}", e.getMessage());
        } catch (NumberFormatException e) {
            log.debug("端口号格式不正确: {}", e.getMessage());
        }

        // 默认端口
        return urlStr.startsWith("https") ? 443 : 80;
    }

    /**
     * 替换URL中的端口
     */
    private String replacePort(String urlStr, int oldPort, int newPort) {
        try {
            URL url = new URL(urlStr);
            String protocol = url.getProtocol();
            String host = url.getHost();
            String path = url.getPath();
            String query = url.getQuery();

            StringBuilder newUrl = new StringBuilder();
            newUrl.append(protocol).append("://").append(host).append(":").append(newPort);

            if (path != null) {
                newUrl.append(path);
            }

            if (query != null) {
                newUrl.append("?").append(query);
            }

            return newUrl.toString();
        } catch (MalformedURLException e) {
            log.error("替换端口失败: {}", e.getMessage());
            // 简单的字符串替换尝试
            return urlStr.replace(":" + oldPort, ":" + newPort);
        }
    }

    /**
     * 处理相对路径
     */
    private String handleRelativePath(String url) {
        String formattedUrl = url;

        // 处理路径格式
        if (url.startsWith("/")) {
            formattedUrl = url.substring(1);
        }

        // 确保包含bucket名称
        if (!formattedUrl.startsWith(bucketName + "/")) {
            formattedUrl = bucketName + "/" + formattedUrl;
        }

        return publicEndpoint + "/" + formattedUrl;
    }

    /**
     * 处理localhost/127.0.0.1替换
     */
    private String handleLocalhostReplacement(String url) {
        if (replaceLocalhost && !StringUtils.isEmpty(localhostReplacement) &&
                (url.contains("localhost") || url.contains("127.0.0.1"))) {
            String finalUrl = url
                    .replace("localhost", localhostReplacement)
                    .replace("127.0.0.1", localhostReplacement);
            log.info("替换localhost/127.0.0.1: {} -> {}", url, finalUrl);
            return finalUrl;
        }
        return url;
    }
}