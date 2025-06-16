package com.quanxiaoha.weblog.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;

import java.util.List;

/**
 * 用户数据访问接口
 * 处理管理员用户信息的查询和密码更新操作
 */
public interface AdminUserDao {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户数据对象
     */
    UserDO selectByUsername(String username);

    /**
     * 更新管理员密码
     * @param userDO 用户数据对象，包含新密码信息
     * @return 影响行数
     */
    int updateAdminPassword(UserDO userDO);
}
