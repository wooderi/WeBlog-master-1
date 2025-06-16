package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanxiaoha.weblog.admin.dao.AdminUserRoleDao;
import com.quanxiaoha.weblog.common.domain.dos.UserRoleDO;
import com.quanxiaoha.weblog.common.domain.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户角色关联数据访问实现类
 * 实现用户与角色之间关联关系的数据库操作
 */
@Service
@Slf4j
public class AdminUserRoleDaoImpl implements AdminUserRoleDao {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户名查询角色关联列表
     * @param username 用户名
     * @return 用户角色关联数据对象列表
     */
    @Override
    public List<UserRoleDO> selectByUsername(String username) {
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUsername, username);
        return userRoleMapper.selectList(wrapper);
    }
}
