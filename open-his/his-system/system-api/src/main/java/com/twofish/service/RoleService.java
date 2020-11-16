package com.twofish.service;


import com.twofish.domain.Role;
import com.twofish.dto.RoleDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface RoleService {

    /**
     * 分页查询角色信息
     * @param roleDto
     * @return
     */
    DataGridView listRoleForPage(RoleDto roleDto);

    /**
     * 添加角色信息
     * @param roleDto
     * @return
     */
    int addRole(RoleDto roleDto);

    /**
     * 根据角色ID查询角色信息
     * @param roleId
     * @return
     */
    Role getRoleById(Long roleId);

    /**
     * 修改角色信息
     * @param roleDto
     * @return
     */
    int updateRole(RoleDto roleDto);

    /**
     * 根据ID删除角色信息
     * @param roleIds
     * @return
     */
    int deleteRoleByIds(Long[] roleIds);


    /**
     * 查询所有可用的角色（不分页用于用户分配角色）
     * @return
     */
    List<Role> selectAllRole();

    /**
     *  保存角色和菜单权限关系
     * @param roleId
     * @param menuIds
     */
    void saveRoleMenu(Long roleId, Long[] menuIds);
}
