package com.sam.project.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sam.enums.MenuTypeEnum;
import com.sam.enums.StatusEnum;
import com.sam.project.sys.model.dto.MenuTreeDTO;
import com.sam.project.sys.model.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户权限菜单
     *
     * @param uid
     * @param statusType
     * @param menuTypes
     * @return
     */
    List<MenuTreeDTO> getUserPermMenus(@Param("uid") Integer uid, @Param("statusType") StatusEnum statusType, @Param("menuTypes") List<MenuTypeEnum> menuTypes);
}
