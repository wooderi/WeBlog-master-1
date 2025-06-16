package com.quanxiaoha.weblog.web.config;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.BlogSettingDO;
import com.quanxiaoha.weblog.common.domain.mapper.BlogSettingMapper;
import com.quanxiaoha.weblog.web.convert.BlogSettingConvert;
import com.quanxiaoha.weblog.web.service.BlogSettingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 提供BlogSettingService bean的配置类
 */
@Configuration
public class BlogSettingServiceConfig {

    @Bean
    public BlogSettingService blogSettingService(BlogSettingMapper blogSettingMapper,
            BlogSettingConvert blogSettingConvert) {
        return new BlogSettingServiceImpl(blogSettingMapper, blogSettingConvert);
    }

    /**
     * 内部实现类，直接在配置类中定义
     */
    public static class BlogSettingServiceImpl extends ServiceImpl<BlogSettingMapper, BlogSettingDO>
            implements BlogSettingService {

        private final BlogSettingConvert blogSettingConvert;

        public BlogSettingServiceImpl(BlogSettingMapper baseMapper, BlogSettingConvert blogSettingConvert) {
            this.baseMapper = baseMapper;
            this.blogSettingConvert = blogSettingConvert;
        }

        @Override
        public Response queryBlogSettingDetail() {
            BlogSettingDO blogSettingDO = getOne(null);
            return Response.success(blogSettingConvert.convert(blogSettingDO));
        }
    }
}