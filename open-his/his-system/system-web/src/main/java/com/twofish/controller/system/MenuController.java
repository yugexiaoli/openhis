package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.constants.Constants;
import com.twofish.domain.Menu;
import com.twofish.dto.DicDataDto;
import com.twofish.dto.MenuDto;
import com.twofish.service.MenuService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "菜单接口",tags = "菜单接口")
@RequestMapping("/system/menu/")
public class MenuController {
    @Resource
    private MenuService menuService;

    /**
     * 查询所有菜单及权限信息
     * @param menuDto
     * @return
     */
    @ApiOperation(value = "查询所有菜单及权限信息",notes = "查询所有菜单及权限信息")
    @GetMapping("listAllMenus")
    public AjaxResult listAllMenus(MenuDto menuDto){
        return AjaxResult.success("查询成功", this.menuService.listAllMenus(menuDto));
    }

    /**
     * 根据ID删除菜单和权限信息(没有子菜单的情况下)
     * @param menuId
     * @return
     */
    @Log(title = "根据ID删除菜单和权限信息",businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据ID删除菜单和权限信息",notes = "根据ID删除菜单和权限信息")
    @DeleteMapping("deleteMenuById/{menuId}")
    public AjaxResult deleteMenuById(@PathVariable Long menuId){
        if( menuService.hasChildByMenuId(menuId)){
            return AjaxResult.fail("删除失败,该菜单含有子节点,请先删除子节点");
        }
        return AjaxResult.toAjax(this.menuService.deleteMenuById(menuId));
    }


    /**
     * 添加菜单和权限信息
     * @param menuDto
     * @return
     */
    @Log(title = "添加菜单和权限信息",businessType = BusinessType.INSERT)
    @PostMapping("addMenu")
    @ApiOperation(value = "添加菜单和权限信息",notes = "添加菜单和权限信息")
    public AjaxResult addMenu(@Validated MenuDto menuDto){
        //设置添加人
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.addMenu(menuDto));
    }


    /**
     * 根据ID查询菜单和权限信息
     * @param menuId
     * @return
     */
    @GetMapping("getMenuById/{menuId}")
    @ApiOperation(value = "根据ID查询菜单和权限信息",notes = "根据ID查询菜单和权限信息")
    public AjaxResult getMenuById(@PathVariable @Validated @NotNull(message = "菜单id不能为空") Long menuId){
        return AjaxResult.success("查询成功",this.menuService.getMenuById(menuId));
    }

    /**
     * 修改菜单和权限信息
     * @param menuDto
     * @return
     */
    @Log(title = "修改菜单和权限信息",businessType = BusinessType.UPDATE)
    @PutMapping("updateMenu")
    @ApiOperation(value = "修改菜单和权限信息",notes = "修改菜单和权限信息")
    public AjaxResult updateMenu(@Validated MenuDto menuDto){
        //设置修改人
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.updateMenu(menuDto));
    }


    /**
     * 查询菜单的下拉树(可用的)
     *
     * @return
     */
    @GetMapping("selectMenuTree")
    @ApiOperation(value = "查询菜单的下拉树",notes = "查询菜单的下拉树")
    public AjaxResult selectMenuTree(){
        MenuDto menuDto = new MenuDto();
        menuDto.setStatus(Constants.STATUS_TRUE);
        List<Menu> menus = this.menuService.listAllMenus(menuDto);
        return AjaxResult.success(menus);
    }

    /**
     * 根据角色ID查询已分配菜单ID[只查子节点]
     * @param roleId
     * @return
     */
    @GetMapping("getMenuIdsByRoleId/{roleId}")
    @ApiOperation(value = "根据角色ID查询已分配菜单ID[只查子节点]",notes = "根据角色ID查询已分配菜单ID[只查子节点]")
    public AjaxResult getMenuIdsByRoleId(@PathVariable  Long roleId){
        return AjaxResult.success("查询成功",this.menuService.getMenuIdsByRoleId(roleId));
    }


}
