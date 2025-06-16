package com.quanxiaoha.weblog.admin.async;

import com.quanxiaoha.weblog.admin.dao.AdminArticleDao;
import com.quanxiaoha.weblog.admin.dao.AdminStatisticsArticlePVDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 文章PV增长异步任务
 * 负责处理文章阅读量和PV统计的异步增长
 */
@Service
@Slf4j
public class PVIncreaseAsyncTask {

    @Autowired
    private AdminArticleDao articleDao;
    @Autowired
    private AdminStatisticsArticlePVDao statisticsArticlePVDao;

    /**
     * 处理文章PV增长
     * @param articleId 文章ID
     * 异步执行以下操作：
     * 1. 增加指定文章的阅读量
     * 2. 增加当天的PV统计
     */
    @Async
    public void handle(Long articleId) {
        log.info("## 文章被阅读量异步 +1, articleId: {}", articleId);
        articleDao.readNumIncrease(articleId);

        Date currDate = new Date();
        log.info("## 文章 PV 异步 +1, currDate: {}", currDate);
        statisticsArticlePVDao.pvIncrease(currDate);
    }
}
