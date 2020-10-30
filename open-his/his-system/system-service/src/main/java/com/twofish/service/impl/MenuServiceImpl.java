package com.twofish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.constants.Constants;
import com.twofish.domain.SimpleUser;
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
}
