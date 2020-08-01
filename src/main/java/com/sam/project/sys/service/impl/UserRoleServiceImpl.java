package com.sam.project.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.sam.project.sys.model.entity.UserRole;
import com.sam.project.sys.mapper.UserRoleMapper;
import com.sam.project.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户角色关系表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
