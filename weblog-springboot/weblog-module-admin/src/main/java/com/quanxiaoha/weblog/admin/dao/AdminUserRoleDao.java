package com.quanxiaoha.weblog.admin.dao;


import com.quanxiaoha.weblog.common.domain.dos.UserRoleDO;

import java.util.List;

/**
 * 用户角色关联数据访问接口
 * 处理用户与角色之间的关联查询操作
 */
public interface AdminUserRoleDao {
    /**
     * 根据用户名查询角色关联
     * @param username 用户名
     * @return 用户角色关联数据对象列表
     */
    List<UserRoleDO> selectByUsername(String username);
}
