package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据菜单id查询它下面子节点的个数
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(Long menuId);

    /**
     * 根据菜单id删除role_menu中间表的数据
     * @param menuId
     */
    void deleteRoleMenuByMenuid(@Param("menuId") Long menuId);

    /**
     * 根据角色ID查询已分配菜单ID[只查子节点]
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
}