package com.quanxiaoha.weblog.admin.service.impl;

import com.quanxiaoha.weblog.admin.dao.AdminUserDao;
import com.quanxiaoha.weblog.admin.dao.AdminUserRoleDao;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;
import com.quanxiaoha.weblog.common.domain.dos.UserRoleDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
/**
 * 用户详情服务实现类
 * 负责加载用户认证信息及角色权限
 */
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminUserDao userDao;
    @Autowired
    private AdminUserRoleDao userRoleDao;

    /**
     * 根据用户名加载用户认证信息及角色权限
     * @param username 用户名
     * @return 用户认证信息，包含用户名、密码和权限列表
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDO userDO = userDao.selectByUsername(username);

        if (userDO == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        // 用户角色
        List<UserRoleDO> roleDOS = userRoleDao.selectByUsername(username);

        String[] roleArr = null;

        if (!CollectionUtils.isEmpty(roleDOS)) {
            List<String> roles = roleDOS.stream().map(p -> p.getRole()).collect(Collectors.toList());
            roleArr = roles.toArray(new String[roles.size()]);
        }

        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(roleArr)
                .build();
    }

}
