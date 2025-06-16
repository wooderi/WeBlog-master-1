package com.quanxiaoha.weblog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quanxiaoha.weblog.admin.model.vo.tag.AddTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.QueryTagPageListReqVO;
import com.quanxiaoha.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.quanxiaoha.weblog.admin.model.vo.user.UpdateAdminPasswordReqVO;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;


/**
 * 管理员用户管理服务接口
 * 负责管理员密码更新等用户相关操作
 */
public interface AdminUserService extends IService<UserDO> {
    /**
     * 更新管理员密码
     * @param updateAdminPasswordReqVO 密码更新请求参数
     * @return 操作结果
     */
    Response updateAdminPassword(UpdateAdminPasswordReqVO updateAdminPasswordReqVO);
}
