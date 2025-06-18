package com.quanxiaoha.weblog.admin.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.admin.dao.AdminTagDao;
import com.quanxiaoha.weblog.admin.dao.AdminUserDao;
import com.quanxiaoha.weblog.common.Response;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;
import com.quanxiaoha.weblog.common.domain.mapper.TagMapper;
import com.quanxiaoha.weblog.common.domain.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class AdminUserDaoImpl implements AdminUserDao {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDO selectByUsername(String username) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 更新管理员密码
     * @param userDO 用户数据对象（包含新密码等信息）
     * @return 影响行数
     */
    @Override
    public int updateAdminPassword(UserDO userDO) {
        UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserDO::getUsername, "admin");
        return userMapper.update(userDO, wrapper);
    }
}
