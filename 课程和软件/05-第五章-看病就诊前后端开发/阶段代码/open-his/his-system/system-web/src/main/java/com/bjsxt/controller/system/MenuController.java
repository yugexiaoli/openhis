package com.bjsxt.controller.system;

import com.bjsxt.constants.Constants;
import com.bjsxt.domain.Menu;
import com.bjsxt.dto.MenuDto;
import com.bjsxt.service.MenuService;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {


    @Autowired
    private MenuService menuService;

    /**
     * 查询所有菜单及权限信息
     */
    @GetMapping("listAllMenus")
    public AjaxResult listAllMenus(MenuDto menuDto){
        List<Menu> list=this.menuService.listAllMenus(menuDto);
        return AjaxResult.success(list);
    }


    /**
     * 查询菜单的下拉树
     */
    @GetMapping("selectMenuTree")
    public AjaxResult selectMenuTree(){
        MenuDto menuDto=new MenuDto();
        menuDto.setStatus(Constants.STATUS_TRUE);//只查询可用的
        return AjaxResult.success(this.menuService.listAllMenus(menuDto));
    }

    /**
     * 添加菜单
     */
    @PostMapping("addMenu")
    public AjaxResult addMenu(@Validated MenuDto menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.addMenu(menuDto));
    }

    /**
     * 修改菜单
     */
    @PutMapping("updateMenu")
    public AjaxResult updateMenu(@Validated MenuDto menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.updateMenu(menuDto));
    }

    /**
     * 根据菜单ID查询一个
     */
    @GetMapping("getMenuById/{menuId}")
    public AjaxResult getMenuById(@PathVariable Long menuId){
        Menu menu=this.menuService.getOne(menuId);
        return AjaxResult.success(menu);
    }

    /**
     * 根据菜单ID删除
     */
    @DeleteMapping("deleteMenuById/{menuId}")
    public AjaxResult deleteMenuById(@PathVariable Long menuId){
        //删除之前要判断当前菜单有没有子节点
        if(this.menuService.hasChildByMenuId(menuId)){
            return AjaxResult.fail("当前要删除的菜单有子节点，请先删除子节点");
        }
        return AjaxResult.toAjax(this.menuService.deleteMenuById(menuId));

    }

    /**
     * 根据角色ID查询菜单权限ID数据
     */
    @GetMapping("getMenuIdsByRoleId/{roleId}")
    public AjaxResult getMenuIdsByRoleId(@PathVariable Long roleId){
        List<Long> ids=this.menuService.getMenusIdsByRoleId(roleId);
        return AjaxResult.success(ids);
    }
}
