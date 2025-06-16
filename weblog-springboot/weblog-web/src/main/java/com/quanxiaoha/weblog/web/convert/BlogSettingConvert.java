package com.quanxiaoha.weblog.web.convert;

import com.quanxiaoha.weblog.common.domain.dos.BlogSettingDO;
import com.quanxiaoha.weblog.web.model.vo.blogsetting.QueryBlogSettingRspVO;

/**
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2023-07-30 8:55
 * @description: 博客设置转换器接口
 **/
public interface BlogSettingConvert {
    /**
     * 将BlogSettingDO转换为QueryBlogSettingRspVO
     * 
     * @param bean 博客设置DO对象
     * @return 博客设置响应VO对象
     */
    QueryBlogSettingRspVO convert(BlogSettingDO bean);
}
