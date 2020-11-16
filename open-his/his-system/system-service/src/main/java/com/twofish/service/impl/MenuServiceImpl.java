package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.constants.Constants;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.MenuDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.domain.Menu;
import com.twofish.mapper.MenuMapper;
import com.twofish.service.MenuService;
@Service
public class MenuServiceImpl implements MenuService{
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> selectMenuTree(Boolean isAdmin, SimpleUser simpleUser) {
        //菜单类型必须是目录或菜单 菜单状态必须是可用
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.in(Menu.COL_MENU_TYPE, Constants.MENU_TYPE_M,Constants.MENU_TYPE_C);
        wrapper.eq(Menu.COL_STATUS,Constants.STATUS_TRUE);
       if(isAdmin){
           //是超级管理员，返回所有菜单
           return this.menuMapper.selectList(wrapper);
       }else {
           //系统用户，根据用户id关联角色菜单来查菜单[未完成]
           return this.menuMapper.selectList(wrapper);
       }
    }

    @Override
    public List<Menu> listAllMenus(MenuDto menuDto) {
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(menuDto.getMenuName()),Menu.COL_MENU_NAME,menuDto.getMenuName());
        qw.eq(null!=menuDto.getStatus(),Menu.COL_STATUS,menuDto.getStatus());
        return menuMapper.selectList(qw);
    }

    @Override
    public int addMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        //设置创建人
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        return menuMapper.insert(menu);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.menuMapper.queryMenuIdsByRoleId(roleId);
    }

    @Override
    public Menu getMenuById(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    @Override
    public int updateMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        //设置修改人
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return menuMapper.updateById(menu);
    }

    @Override
    public int deleteMenuById(Long menuId) {
        //删除role_menu中间表的数据，再删除菜单
        this.menuMapper.deleteRoleMenuByMenuid(menuId);
        return menuMapper.deleteById(menuId);
    }

    @Override
    public Boolean hasChildByMenuId(Long menuId) {
        return  menuMapper.queryChildCountByMenuId(menuId) > 0L ? true :false;
    }
}
