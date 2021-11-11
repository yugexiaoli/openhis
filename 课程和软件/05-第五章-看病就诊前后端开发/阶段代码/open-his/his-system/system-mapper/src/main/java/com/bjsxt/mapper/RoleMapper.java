package com.bjsxt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjsxt.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色IDS删除sys_role_menu中间表的数据
     *
     * @param ids
     */
    void deleteRoleMenuByRoleIds(@Param("ids") List<Long> ids);

    /**
     * 根据角色IDS删除sys_role_user中间表的数据
     *
     * @param ids
     */
    void deleteRoleUserByRoleIds(@Param("ids") List<Long> ids);

    /**
     * 保存角色和菜单的关系到数据库
     *
     * @param roleId
     * @param menuId
     */
    void saveRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    /**
     * 根据用户IDS删除sys_role_user里面的数据
     *
     * @param ids
     */
    void deleteRoleUserByUserIds(@Param("ids") List<Long> ids);

    /**
     * 根据菜单权限ID删除sys_role_menu
     */
    void deleteRoleMenuByMenuIds(@Param("ids") List<Long> ids);

    /**
     * 根据用户ID查询用户拥有的角色IDS
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 保存角色和用户之间的关系
     *
     * @param userId
     * @param roleId
     */
    void saveRoleUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}