package com.bjsxt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjsxt.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据菜单ID查询当前菜单的子节点的个数
     *
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据角色ID查询所有选中的权限菜单ID【只查子节点的】
     *
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
}