package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.quanxiaoha.weblog.common.PageResponse;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.*;
import com.quanxiaoha.weblog.common.enums.EventEnum;
import com.quanxiaoha.weblog.common.eventbus.ArticleEvent;
import com.quanxiaoha.weblog.common.exception.ResourceNotFoundException;
import com.quanxiaoha.weblog.web.convert.ArticleConvert;
import com.quanxiaoha.weblog.web.dao.*;
import com.quanxiaoha.weblog.web.model.dto.DeepseekSummaryRequestDTO;
import com.quanxiaoha.weblog.web.model.dto.DeepseekSummaryResponseDTO;
import com.quanxiaoha.weblog.web.model.vo.article.*;
import com.quanxiaoha.weblog.web.model.vo.category.QueryCategoryListItemRspVO;
import com.quanxiaoha.weblog.web.model.vo.tag.QueryTagListItemRspVO;
import com.quanxiaoha.weblog.web.service.ArticleService;
import com.quanxiaoha.weblog.web.utils.MarkdownUtil;
import com.quanxiaoha.weblog.web.utils.MinioUrlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleContentDao articleContentDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleCategoryRelDao articleCategoryRelDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private ArticleTagRelDao articleTagRelDao;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private ArticleConvert articleConvert;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MinioUrlConverter minioUrlConverter;

    @Override
    public PageResponse queryIndexArticlePageList(QueryIndexArticlePageListReqVO queryIndexArticlePageListReqVO) {
        Long current = queryIndexArticlePageListReqVO.getCurrent();
        Long size = queryIndexArticlePageListReqVO.getSize();

        IPage<ArticleDO> articleDOIPage = articleDao.queryArticlePageList(current, size);
        List<ArticleDO> records = articleDOIPage.getRecords();

        List<QueryIndexArticlePageItemRspVO> list = null;
        if (!CollectionUtils.isEmpty(records)) {
            list = records.stream()
                    .map(articleDO -> articleConvert.convert(articleDO))
                    .collect(Collectors.toList());

            List<Long> articleIds = list.stream().map(p -> p.getId()).collect(Collectors.toList());

            // 设置分类信息
            List<CategoryDO> categoryDOS = categoryDao.selectAllCategory();
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).findFirst();
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    String categoryName = categoryIdNameMap.get(categoryId);

                    QueryCategoryListItemRspVO queryCategoryListItemRspVO = QueryCategoryListItemRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    p.setCategory(queryCategoryListItemRspVO);
                }
                return p;
            }).collect(Collectors.toList());

            // 设置标签信息
            List<TagDO> tagDOS = tagDao.selectAllTag();
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                List<ArticleTagRelDO> searchArticleTagList = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).collect(Collectors.toList());

                List<QueryTagListItemRspVO> queryTagListItemRspVOS = Lists.newArrayList();
                searchArticleTagList.forEach(rel -> {
                    Long tagId = rel.getTagId();
                    String tagName = tagIdNameMap.get(tagId);

                    QueryTagListItemRspVO queryTagListItemRspVO = QueryTagListItemRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    queryTagListItemRspVOS.add(queryTagListItemRspVO);
                });

                p.setTags(queryTagListItemRspVOS);

                // 转换图片URL
                if (p.getTitleImage() != null && !p.getTitleImage().isEmpty()) {
                    String originalUrl = p.getTitleImage();
                    String convertedUrl = minioUrlConverter.convert(originalUrl);
                    p.setTitleImage(convertedUrl);
                    log.info("文章图片URL转换: {} -> {}", originalUrl, convertedUrl);
                }

                return p;
            }).collect(Collectors.toList());
        }
        return PageResponse.success(articleDOIPage, list);
    }

    @Override
    public PageResponse queryCategoryArticlePageList(
            QueryCategoryArticlePageListReqVO queryCategoryArticlePageListReqVO) {
        Long current = queryCategoryArticlePageListReqVO.getCurrent();
        Long size = queryCategoryArticlePageListReqVO.getSize();
        Long queryCategoryId = queryCategoryArticlePageListReqVO.getCategoryId();

        List<ArticleCategoryRelDO> articleCategoryRelDOList = articleCategoryRelDao.selectByCategoryId(queryCategoryId);

        // 判断该分类下是否存在文章
        if (CollectionUtils.isEmpty(articleCategoryRelDOList)) {
            return PageResponse.success(null, null);
        }

        List<Long> categoryArticleIds = articleCategoryRelDOList.stream().map(p -> p.getArticleId())
                .collect(Collectors.toList());

        IPage<ArticleDO> articleDOIPage = articleDao.queryArticlePageListByArticleIds(current, size,
                categoryArticleIds);
        List<ArticleDO> records = articleDOIPage.getRecords();

        List<QueryIndexArticlePageItemRspVO> list = null;
        if (!CollectionUtils.isEmpty(records)) {
            list = records.stream()
                    .map(articleDO -> articleConvert.convert(articleDO))
                    .collect(Collectors.toList());

            List<Long> articleIds = list.stream().map(p -> p.getId()).collect(Collectors.toList());

            // 设置分类信息
            List<CategoryDO> categoryDOS = categoryDao.selectAllCategory();
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).findFirst();
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    String categoryName = categoryIdNameMap.get(categoryId);

                    QueryCategoryListItemRspVO queryCategoryListItemRspVO = QueryCategoryListItemRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    p.setCategory(queryCategoryListItemRspVO);
                }
                return p;
            }).collect(Collectors.toList());

            // 设置标签信息
            List<TagDO> tagDOS = tagDao.selectAllTag();
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                List<ArticleTagRelDO> searchArticleTagList = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).collect(Collectors.toList());

                List<QueryTagListItemRspVO> queryTagListItemRspVOS = Lists.newArrayList();
                searchArticleTagList.forEach(rel -> {
                    Long tagId = rel.getTagId();
                    String tagName = tagIdNameMap.get(tagId);

                    QueryTagListItemRspVO queryTagListItemRspVO = QueryTagListItemRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    queryTagListItemRspVOS.add(queryTagListItemRspVO);
                });

                p.setTags(queryTagListItemRspVOS);

                // 转换图片URL
                if (p.getTitleImage() != null && !p.getTitleImage().isEmpty()) {
                    String originalUrl = p.getTitleImage();
                    String convertedUrl = minioUrlConverter.convert(originalUrl);
                    p.setTitleImage(convertedUrl);
                    log.info("文章图片URL转换: {} -> {}", originalUrl, convertedUrl);
                }

                return p;
            }).collect(Collectors.toList());
        }
        return PageResponse.success(articleDOIPage, list);
    }

    @Override
    public Response queryArticleDetail(QueryArticleDetailReqVO queryArticleDetailReqVO) {
        Long articleId = queryArticleDetailReqVO.getArticleId();

        // 判断文章是否存在
        ArticleDO articleDO = articleDao.selectArticleById(articleId);

        if (Objects.isNull(articleDO)) {
            throw new ResourceNotFoundException();
        }

        ArticleContentDO articleContentDO = articleContentDao.selectArticleContentByArticleId(articleId);

        // 先创建VO对象，设置基本属性
        QueryArticleDetailRspVO vo = QueryArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .updateTime(articleDO.getUpdateTime())
                .content(MarkdownUtil.parse2HtmlWithUrlConverting(articleContentDO.getContent(), minioUrlConverter))
                .readNum(articleDO.getReadNum())
                .build();

        // 处理标题图片URL
        if (articleDO.getTitleImage() != null && !articleDO.getTitleImage().isEmpty()) {
            String originalUrl = articleDO.getTitleImage();
            String convertedUrl = minioUrlConverter.convert(originalUrl);
            // 设置转换后的URL到文章对象，以便前端显示
            vo.setTitleImage(convertedUrl);
            log.info("文章详情标题图片URL转换: {} -> {}", originalUrl, convertedUrl);
        }

        // 查询文章所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelDao.selectByArticleId(articleId);
        CategoryDO categoryDO = categoryDao.selectByCategoryId(articleCategoryRelDO.getCategoryId());
        vo.setCategoryId(categoryDO.getId());
        vo.setCategoryName(categoryDO.getName());

        // 查询文章标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDao.selectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(p -> p.getTagId()).collect(Collectors.toList());
        List<TagDO> tagDOS = tagDao.selectByTagIds(tagIds);

        List<QueryTagListItemRspVO> queryTagListItemRspVOS = tagDOS.stream()
                .map(p -> QueryTagListItemRspVO.builder().id(p.getId()).name(p.getName()).build())
                .collect(Collectors.toList());
        vo.setTags(queryTagListItemRspVOS);

        // 上一篇
        ArticleDO preArticle = articleDao.selectPreArticle(articleId);
        if (Objects.nonNull(preArticle)) {
            QueryArticleLinkRspVO prevArticleVO = QueryArticleLinkRspVO.builder()
                    .title(preArticle.getTitle())
                    .id(preArticle.getId())
                    .build();
            vo.setPreArticle(prevArticleVO);
        }

        // 下一篇
        ArticleDO nextArticle = articleDao.selectNextArticle(articleId);
        if (Objects.nonNull(nextArticle)) {
            QueryArticleLinkRspVO nextArticleVO = QueryArticleLinkRspVO.builder()
                    .title(nextArticle.getTitle())
                    .id(nextArticle.getId())
                    .build();
            vo.setNextArticle(nextArticleVO);
        }

        // 发送消息事件
        log.info("发送 PV +1 消息事件");
        eventBus.post(ArticleEvent.builder().articleId(articleId).message(EventEnum.PV_INCREASE.getMessage()).build());

        return Response.success(vo);
    }

    @Override
    public PageResponse queryTagArticlePageList(QueryTagArticlePageListReqVO queryTagArticlePageListReqVO) {
        Long current = queryTagArticlePageListReqVO.getCurrent();
        Long size = queryTagArticlePageListReqVO.getSize();
        Long queryTagId = queryTagArticlePageListReqVO.getTagId();

        List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDao.selectByTagId(queryTagId);

        // 判断该分类下是否存在文章
        if (CollectionUtils.isEmpty(articleTagRelDOList)) {
            return PageResponse.success(null, null);
        }

        List<Long> tagArticleIds = articleTagRelDOList.stream().map(p -> p.getArticleId()).collect(Collectors.toList());

        IPage<ArticleDO> articleDOIPage = articleDao.queryArticlePageListByArticleIds(current, size, tagArticleIds);
        List<ArticleDO> records = articleDOIPage.getRecords();

        List<QueryIndexArticlePageItemRspVO> list = null;
        if (!CollectionUtils.isEmpty(records)) {
            list = records.stream()
                    .map(articleDO -> articleConvert.convert(articleDO))
                    .collect(Collectors.toList());

            List<Long> articleIds = list.stream().map(p -> p.getId()).collect(Collectors.toList());

            // 设置分类信息
            List<CategoryDO> categoryDOS = categoryDao.selectAllCategory();
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).findFirst();
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    String categoryName = categoryIdNameMap.get(categoryId);

                    QueryCategoryListItemRspVO queryCategoryListItemRspVO = QueryCategoryListItemRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    p.setCategory(queryCategoryListItemRspVO);
                }
                return p;
            }).collect(Collectors.toList());

            // 设置标签信息
            List<TagDO> tagDOS = tagDao.selectAllTag();
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                List<ArticleTagRelDO> searchArticleTagList = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).collect(Collectors.toList());

                List<QueryTagListItemRspVO> queryTagListItemRspVOS = Lists.newArrayList();
                searchArticleTagList.forEach(rel -> {
                    Long tagId = rel.getTagId();
                    String tagName = tagIdNameMap.get(tagId);

                    QueryTagListItemRspVO queryTagListItemRspVO = QueryTagListItemRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    queryTagListItemRspVOS.add(queryTagListItemRspVO);
                });

                p.setTags(queryTagListItemRspVOS);

                // 转换图片URL
                if (p.getTitleImage() != null && !p.getTitleImage().isEmpty()) {
                    String originalUrl = p.getTitleImage();
                    String convertedUrl = minioUrlConverter.convert(originalUrl);
                    p.setTitleImage(convertedUrl);
                    log.info("文章图片URL转换: {} -> {}", originalUrl, convertedUrl);
                }

                return p;
            }).collect(Collectors.toList());
        }
        return PageResponse.success(articleDOIPage, list);
    }

    @Override
    public Response generateArticleSummary(GenerateArticleSummaryReqVO generateArticleSummaryReqVO) {
        Long articleId = generateArticleSummaryReqVO.getArticleId();

        // 查询文章是否存在
        ArticleDO articleDO = articleDao.selectArticleById(articleId);
        if (articleDO == null) {
            return Response.fail("404", "文章不存在");
        }

        // 查询文章内容
        ArticleContentDO articleContentDO = articleContentDao.selectArticleContentByArticleId(articleId);
        if (articleContentDO == null) {
            return Response.fail("404", "文章内容不存在");
        }

        String content = articleContentDO.getContent();

        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer sk-517de0aaf0834110bec468122242a32b");
            headers.set("Content-Type", "application/json");

            // 构建消息列表
            List<Map<String, String>> messages = new ArrayList<>();

            // 添加系统消息
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个专业的文章摘要生成助手。请对以下文章内容生成一个简洁、全面的摘要，突出文章的主要观点和结论。");
            messages.add(systemMessage);

            // 添加用户消息
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", "请为以下文章生成摘要：\n\n" + content);
            messages.add(userMessage);

            // 构建请求体
            DeepseekSummaryRequestDTO requestDTO = DeepseekSummaryRequestDTO.builder()
                    .model("deepseek-chat") // 使用 DeepSeek-V3 模型
                    .messages(messages)
                    .stream(false) // 非流式输出
                    .build();

            // 发送请求
            HttpEntity<DeepseekSummaryRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
            ResponseEntity<DeepseekSummaryResponseDTO> responseEntity = restTemplate.postForEntity(
                    "https://api.deepseek.com/chat/completions",
                    entity,
                    DeepseekSummaryResponseDTO.class);

            // 处理响应
            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                DeepseekSummaryResponseDTO responseDTO = responseEntity.getBody();

                // 提取回复内容
                String summary = "";
                if (responseDTO.getChoices() != null && !responseDTO.getChoices().isEmpty()) {
                    DeepseekSummaryResponseDTO.Message message = responseDTO.getChoices().get(0).getMessage();
                    if (message != null) {
                        summary = message.getContent();
                    }
                }

                if (summary.isEmpty()) {
                    return Response.fail("500", "获取摘要内容为空");
                }

                // 构建返回结果
                GenerateArticleSummaryRspVO rspVO = GenerateArticleSummaryRspVO.builder()
                        .summary(summary)
                        .build();

                return Response.success(rspVO);
            } else {
                return Response.fail("502", "调用摘要生成服务失败");
            }
        } catch (RestClientException e) {
            log.error("调用Deepseek API失败", e);
            return Response.fail("502", "调用摘要生成服务失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("生成文章摘要失败", e);
            return Response.fail("500", "生成文章摘要失败: " + e.getMessage());
        }
    }

    @Override
    public PageResponse querySearchArticlePageList(QuerySearchArticlePageListReqVO querySearchArticlePageListReqVO) {
        Long current = querySearchArticlePageListReqVO.getCurrent();
        Long size = querySearchArticlePageListReqVO.getSize();
        String keyword = querySearchArticlePageListReqVO.getKeyword();

        IPage<ArticleDO> articleDOIPage = articleDao.queryArticlePageListByKeyword(current, size, keyword);
        List<ArticleDO> records = articleDOIPage.getRecords();

        List<QueryIndexArticlePageItemRspVO> list = null;
        if (!CollectionUtils.isEmpty(records)) {
            list = records.stream()
                    .map(articleDO -> articleConvert.convert(articleDO))
                    .collect(Collectors.toList());

            List<Long> articleIds = list.stream().map(p -> p.getId()).collect(Collectors.toList());

            // 设置分类信息
            List<CategoryDO> categoryDOS = categoryDao.selectAllCategory();
            Map<Long, String> categoryIdNameMap = categoryDOS.stream()
                    .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).findFirst();
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    String categoryName = categoryIdNameMap.get(categoryId);

                    QueryCategoryListItemRspVO queryCategoryListItemRspVO = QueryCategoryListItemRspVO.builder()
                            .id(categoryId)
                            .name(categoryName)
                            .build();
                    p.setCategory(queryCategoryListItemRspVO);
                }
                return p;
            }).collect(Collectors.toList());

            // 设置标签信息
            List<TagDO> tagDOS = tagDao.selectAllTag();
            Map<Long, String> tagIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDao.selectByArticleIds(articleIds);
            list = list.stream().map(p -> {
                Long articleId = p.getId();
                List<ArticleTagRelDO> searchArticleTagList = articleTagRelDOS.stream()
                        .filter(rel -> Objects.equals(rel.getArticleId(), articleId)).collect(Collectors.toList());

                List<QueryTagListItemRspVO> queryTagListItemRspVOS = Lists.newArrayList();
                searchArticleTagList.forEach(rel -> {
                    Long tagId = rel.getTagId();
                    String tagName = tagIdNameMap.get(tagId);

                    QueryTagListItemRspVO queryTagListItemRspVO = QueryTagListItemRspVO.builder()
                            .id(tagId)
                            .name(tagName)
                            .build();
                    queryTagListItemRspVOS.add(queryTagListItemRspVO);
                });

                p.setTags(queryTagListItemRspVOS);

                // 转换图片URL
                if (p.getTitleImage() != null && !p.getTitleImage().isEmpty()) {
                    String originalUrl = p.getTitleImage();
                    String convertedUrl = minioUrlConverter.convert(originalUrl);
                    p.setTitleImage(convertedUrl);
                    log.info("文章图片URL转换: {} -> {}", originalUrl, convertedUrl);
                }

                return p;
            }).collect(Collectors.toList());
        }
        return PageResponse.success(articleDOIPage, list);
    }
}
