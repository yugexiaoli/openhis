package com.bjsxt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.SimpleUser;
import com.bjsxt.dto.BaseDto;
import com.bjsxt.dto.MenuDto;
import com.bjsxt.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.Menu;
import com.bjsxt.mapper.MenuMapper;
import com.bjsxt.service.MenuService;
/**
* @Author: 尚学堂 雷哥
*/

@Service
public class MenuServiceImpl  implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser) {
        QueryWrapper<Menu> qw=new QueryWrapper<>();
        qw.eq(Menu.COL_STATUS, Constants.STATUS_TRUE);
        qw.in(Menu.COL_MENU_TYPE,Constants.MENU_TYPE_M,Constants.MENU_TYPE_C);
        qw.orderByAsc(Menu.COL_PARENT_ID);
        if(isAdmin){
            return menuMapper.selectList(qw);
        }else{
            //根据用户id查询用户拥有的菜单信息
            return menuMapper.selectList(qw);
        }
    }

    @Override
    public List<Menu> listAllMenus(MenuDto menuDto) {
        QueryWrapper<Menu> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(menuDto.getMenuName()),Menu.COL_MENU_NAME,menuDto.getMenuName());
        qw.eq(StringUtils.isNotBlank(menuDto.getStatus()),Menu.COL_STATUS,menuDto.getStatus());
        return this.menuMapper.selectList(qw);
    }

    @Override
    public int addMenu(MenuDto menuDto) {
        Menu menu=new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        //设置创建人和创建时间
        menu.setCreateTime(DateUtil.date());
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        return this.menuMapper.insert(menu);
    }

    @Override
    public int updateMenu(MenuDto menuDto) {
        Menu menu=new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        //设置修改人
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return this.menuMapper.updateById(menu);
    }

    @Override
    public Menu getOne(Long menuId) {
        return this.menuMapper.selectById(menuId);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        Long count=this.menuMapper.queryChildCountByMenuId(menuId);
        return count>0L?true:false;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        //删除sys_role_menu中间表的数据[后面完成]
        this.roleMapper.deleteRoleMenuByMenuIds(Arrays.asList(menuId));
        return this.menuMapper.deleteById(menuId);
    }

    @Override
    public List<Long> getMenusIdsByRoleId(Long roleId) {
        return this.menuMapper.queryMenuIdsByRoleId(roleId);
    }
}
