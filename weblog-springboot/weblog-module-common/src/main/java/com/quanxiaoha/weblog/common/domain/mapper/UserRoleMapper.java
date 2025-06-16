package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quanxiaoha.weblog.common.domain.dos.UserDO;
import com.quanxiaoha.weblog.common.domain.dos.UserRoleDO;

/**
 * 用户角色关联关系数据访问层
 * 提供用户与角色关联关系的CRUD操作
 */
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {
}
