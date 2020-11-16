package com.twofish.service;

import com.twofish.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.MenuDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface MenuService {

    /**
     * 查询菜单的下拉树,根据用户查询菜单返回
     * 如果是超级管理员，返回所有菜单权限
     * 如果不是超级管理员，那就得看用户id，根据id关联角色菜单
     * @param isAdmin 是否超级管理员
     * @param simpleUser 里面有用户id
     * @return
     */
    public List<Menu> selectMenuTree(Boolean isAdmin, SimpleUser simpleUser);

    /**
     * 查询所有菜单及权限信息
     * @param menuDto
     * @return
     */
    List<Menu> listAllMenus(MenuDto menuDto);


    /**
     * 添加菜单和权限信息
     * @param menuDto
     * @return
     */
    int addMenu(MenuDto menuDto);

    /**
     * 根据ID查询菜单和权限信息
     * @param menuId
     * @return
     */
    Menu getMenuById(Long menuId);


    /**
     * 修改菜单和权限信息
     * @param menuDto
     * @return
     */
    int updateMenu(MenuDto menuDto);


    /**
     * 根据ID删除菜单和权限信息(在没有子节点的情况下)
     * @param menuId
     * @return
     */
    int deleteMenuById(Long menuId);


    /**
     * 根据菜单ID判断菜单是否有子节点
     * @param menuId
     * @return
     */
    Boolean hasChildByMenuId(Long menuId);

    /**
     * 根据角色ID查询已分配菜单ID[只查子节点]
     * @param roleId
     * @return
     */
    List<Long> getMenuIdsByRoleId(Long roleId);
}
