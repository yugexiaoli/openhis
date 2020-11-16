package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色ids集合删除role_menu中间表的数据
     * @param ids
     */
    void deleteRoleMenuByRoleids(@Param("ids") List<Long> ids);

    /**
     * 根据角色ids集合删除role_user中间表的数据
     * @param ids
     */
    void deleteRoleUserByRoleids(@Param("ids") List<Long> ids);

    /**
     * 添加一个role_menu权限信息
     * @param roleId
     * @param menuId
     */
    void insertRoleMenu(@Param("roleId") Long roleId,@Param("menuId") Long menuId);

    /**
     * 根据用户ids删除sys_role_user数据
     * @param ids
     */
    void deleteRoleUserByUserids(@Param("ids") List<Long> ids);
}