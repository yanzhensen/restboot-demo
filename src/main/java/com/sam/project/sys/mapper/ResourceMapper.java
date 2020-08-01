package com.sam.project.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sam.project.sys.model.dto.ResourcePermDTO;
import com.sam.project.sys.model.entity.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 获取用户权限
     *
     * @param uid
     * @return
     */
    List<ResourcePermDTO> getUserResourcePerms(@Param("uid") Integer uid);

    /**
     * 获取用户菜单资源权限
     *
     * @param uid
     * @return
     */
    List<ResourcePermDTO> getUserMenuResourcePerms(@Param("uid") Integer uid);

}
