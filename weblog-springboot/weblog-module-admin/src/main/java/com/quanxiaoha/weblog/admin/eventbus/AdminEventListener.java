package com.quanxiaoha.weblog.admin.eventbus;

import com.google.common.eventbus.Subscribe;
import com.quanxiaoha.weblog.admin.async.PVIncreaseAsyncTask;
import com.quanxiaoha.weblog.common.eventbus.EventListener;
import com.quanxiaoha.weblog.common.eventbus.ArticleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 事件监听
 **/
/**
 * 管理后台事件监听器
 * 处理文章相关事件，如阅读量(PV)统计更新
 */
@Component
@Slf4j
public class AdminEventListener implements EventListener {

    @Autowired
    private PVIncreaseAsyncTask pvIncreaseAsyncTask;

    /**
     * 处理文章事件
     * 当文章被访问时，异步更新文章阅读量统计
     * @param event 文章事件对象，包含文章ID和事件消息
     */
    @Subscribe
    @Override
    public void handleEvent(ArticleEvent event) {
        Long articleId = event.getArticleId();
        String message = event.getMessage();
        // 处理事件
        log.info("==> Received event: {}", message);
        pvIncreaseAsyncTask.handle(articleId);
    }
}
