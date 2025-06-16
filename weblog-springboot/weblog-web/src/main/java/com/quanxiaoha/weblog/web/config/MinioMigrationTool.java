package com.quanxiaoha.weblog.web.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleContentDO;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleContentMapper;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleMapper;
import com.quanxiaoha.weblog.web.utils.MinioUrlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minio图片URL迁移工具
 * 用于修复数据库中使用9005端口的图片URL
 */
@Component
@Slf4j
@Order(2) // 在MinioInitializer之后执行
public class MinioMigrationTool implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private MinioUrlConverter minioUrlConverter;

    @Value("${minio.endpoint}")
    private String apiEndpoint;

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint;

    // 用于查找Markdown中的图片链接
    private static final Pattern MARKDOWN_IMAGE_PATTERN = Pattern.compile("!\\[(.*?)\\]\\((http[^\\)]+)\\)");
    // 用于查找HTML中的img标签
    private static final Pattern HTML_IMAGE_PATTERN = Pattern.compile("<img[^>]+src=['\"]([^'\"]+)['\"][^>]*>");

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("开始迁移Minio图片URL (9005 -> 9000)...");

            // 测试URL转换功能
            testUrlConverter();

            // 迁移文章标题图片
            migrateArticleImages();

            // 迁移文章内容中的图片
            migrateArticleContentImages();

            log.info("Minio图片URL迁移完成！");
        } catch (Exception e) {
            log.error("Minio图片URL迁移失败", e);
        }
    }

    /**
     * 测试URL转换功能
     */
    private void testUrlConverter() {
        log.info("=== 测试URL转换功能 ===");

        // 测试用例
        String[] testUrls = {
                "http://127.0.0.1:9005/weblog/image.png",
                "http://localhost:9005/weblog/image.png",
                "http://127.0.0.1:9000/weblog/image.png",
                "http://example.com/images/pic.jpg", // 非Minio URL
                "/weblog/image.png", // 相对路径
                "image.png" // 无路径
        };

        for (String url : testUrls) {
            String converted = minioUrlConverter.convert(url);
            log.info("URL转换测试: {} -> {}", url, converted);
        }

        log.info("=== URL转换测试完成 ===");
    }

    /**
     * 迁移文章标题图片URL
     */
    private void migrateArticleImages() {
        // 查询所有文章
        QueryWrapper<ArticleDO> queryWrapper = new QueryWrapper<>();
        List<ArticleDO> articles = articleMapper.selectList(queryWrapper);

        int migratedCount = 0;

        for (ArticleDO article : articles) {
            String titleImage = article.getTitleImage();
            if (titleImage != null && !titleImage.isEmpty()) {
                // 检查是否包含API端点或9005端口
                if (titleImage.contains(apiEndpoint) || titleImage.contains(":9005/")) {
                    // 使用URL转换器处理
                    String newTitleImage = minioUrlConverter.convert(titleImage);

                    if (!titleImage.equals(newTitleImage)) {
                        // 更新文章
                        ArticleDO updateArticle = ArticleDO.builder()
                                .id(article.getId())
                                .titleImage(newTitleImage)
                                .build();

                        int rows = articleMapper.updateById(updateArticle);
                        if (rows > 0) {
                            migratedCount++;
                            log.info("已修复文章(ID={})图片URL: {} -> {}", article.getId(), titleImage, newTitleImage);
                        }
                    }
                }
            }
        }

        log.info("共修复 {} 篇文章的标题图片URL", migratedCount);
    }

    /**
     * 迁移文章内容中的图片URL
     */
    private void migrateArticleContentImages() {
        // 查询所有文章内容
        QueryWrapper<ArticleContentDO> queryWrapper = new QueryWrapper<>();
        List<ArticleContentDO> articleContents = articleContentMapper.selectList(queryWrapper);

        int migratedCount = 0;

        for (ArticleContentDO articleContent : articleContents) {
            String content = articleContent.getContent();
            if (content == null || content.isEmpty()) {
                continue;
            }

            boolean needsUpdate = false;
            StringBuilder updatedContent = new StringBuilder(content);

            // 处理Markdown格式的图片链接
            Matcher markdownMatcher = MARKDOWN_IMAGE_PATTERN.matcher(content);
            while (markdownMatcher.find()) {
                String imageUrl = markdownMatcher.group(2);
                // 检查是否包含API端点或9005端口
                if (imageUrl.contains(apiEndpoint) || imageUrl.contains(":9005/")) {
                    String convertedUrl = minioUrlConverter.convert(imageUrl);
                    if (!imageUrl.equals(convertedUrl)) {
                        // 替换原始内容中的URL
                        int startPos = markdownMatcher.start(2);
                        int endPos = markdownMatcher.end(2);
                        updatedContent.replace(startPos, endPos, convertedUrl);
                        needsUpdate = true;
                        log.info("修复文章内容(ID={})中的Markdown图片URL: {} -> {}",
                                articleContent.getArticleId(), imageUrl, convertedUrl);
                    }
                }
            }

            // 处理HTML格式的图片链接
            Matcher htmlMatcher = HTML_IMAGE_PATTERN.matcher(content);
            while (htmlMatcher.find()) {
                String imageUrl = htmlMatcher.group(1);
                // 检查是否包含API端点或9005端口
                if (imageUrl.contains(apiEndpoint) || imageUrl.contains(":9005/")) {
                    String convertedUrl = minioUrlConverter.convert(imageUrl);
                    if (!imageUrl.equals(convertedUrl)) {
                        // 替换原始内容中的URL
                        int startPos = htmlMatcher.start(1);
                        int endPos = htmlMatcher.end(1);
                        updatedContent.replace(startPos, endPos, convertedUrl);
                        needsUpdate = true;
                        log.info("修复文章内容(ID={})中的HTML图片URL: {} -> {}",
                                articleContent.getArticleId(), imageUrl, convertedUrl);
                    }
                }
            }

            // 如果内容被修改，更新数据库
            if (needsUpdate) {
                ArticleContentDO updateContent = ArticleContentDO.builder()
                        .id(articleContent.getId())
                        .articleId(articleContent.getArticleId())
                        .content(updatedContent.toString())
                        .build();

                int rows = articleContentMapper.updateById(updateContent);
                if (rows > 0) {
                    migratedCount++;
                }
            }
        }

        log.info("共修复 {} 篇文章内容中的图片URL", migratedCount);
    }
}