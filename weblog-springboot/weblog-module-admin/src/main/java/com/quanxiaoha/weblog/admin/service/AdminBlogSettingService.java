package com.quanxiaoha.weblog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quanxiaoha.weblog.admin.model.vo.blogsetting.UpdateBlogSettingReqVO;
import com.quanxiaoha.weblog.admin.model.vo.user.QueryUserDetailRspVO;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.BlogSettingDO;


/**
 * 博客设置管理服务接口
 * 负责博客基本信息的配置与查询
 */
public interface AdminBlogSettingService extends IService<BlogSettingDO> {

    /**
     * 更新博客设置信息
     * @param updateBlogSettingReqVO 博客设置更新请求参数
     * @return 操作结果
     */
    Response updateBlogSetting(UpdateBlogSettingReqVO updateBlogSettingReqVO);

    /**
     * 查询博客设置详情
     * @return 博客设置详情响应
     */
    Response queryBlogSettingDetail();

    /**
     * 查询用户昵称和头像信息
     * @return 包含用户昵称和头像的响应
     */
    Response<QueryUserDetailRspVO> queryNicknameAndAvatar();
}
