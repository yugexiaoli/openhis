package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.RoleDto;
import com.twofish.service.RoleService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * 角色接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "角色接口",tags = "角色接口")
@RequestMapping("/system/role/")
public class RoleController {
    @Resource
    private RoleService roleservice;

    /**
     * 分页查询角色信息
     * @param roledto
     * @return
     */
    @GetMapping("listRoleForPage")
    @ApiOperation(value = "分页查询角色信息",notes = "分页查询角色信息")
    public AjaxResult listRoleForPage(RoleDto roledto){
        DataGridView dataGridView = this.roleservice.listRoleForPage(roledto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 查询所有可用角色
     *
     * @return
     */
    @GetMapping("selectAllRole")
    @ApiOperation(value = "查询所有可用角色",notes = "查询所有可用角色")
    public AjaxResult selectAllRole(){
        return AjaxResult.success("查询成功",this.roleservice.selectAllRole());
    }

    /**
     * 添加角色
     * @param roledto
     * @return
     */
    @Log(title = "添加角色",businessType = BusinessType.INSERT)
    @PostMapping("addRole")
    @ApiOperation(value = "添加角色",notes = "添加角色")
    public AjaxResult addRole(@Validated RoleDto roledto){
        //设置添加人
        roledto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleservice.addRole(roledto));
    }


    /**
     * 根据角色ID查询角色信息
     * @param roleId
     * @return
     */
    @GetMapping("getRoleById/{roleId}")
    @ApiOperation(value = "根据角色ID查询角色信息",notes = "根据角色ID查询角色信息")
    public AjaxResult getRoleById(@PathVariable Long roleId){
        return AjaxResult.success("查询成功",this.roleservice.getRoleById(roleId));
    }

    /**
     * 修改角色信息
     * @param roledto
     * @return
     */
    @Log(title = "修改角色信息",businessType = BusinessType.UPDATE)
    @PutMapping("updateRole")
    @ApiOperation(value = "修改角色信息",notes = "修改角色信息")
    public AjaxResult updateRole(@Validated RoleDto roledto){
        //设置修改人
        roledto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleservice.updateRole(roledto));
    }


    /**
     * 根据ID删除角色信息(可批量删除)
     * @param roleIds 角色id数组
     * @return
     */
    @Log(title = "根据ID删除角色信息",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteRoleByIds/{roleIds}")
    @ApiOperation(value = "根据ID删除角色信息",notes = "根据ID删除角色信息")
    public AjaxResult deleteRoleByIds(@PathVariable Long[] roleIds){
        return AjaxResult.toAjax(this.roleservice.deleteRoleByIds(roleIds));
    }


    /**
     *  保存角色和菜单权限关系
     * @param roleId
     * @param menuIds
     * @return
     */
    @Log(title = "保存角色和菜单权限关系",businessType = BusinessType.INSERT)
    @PostMapping("saveRoleMenu/{roleId}/{menuIds}")
    @ApiOperation(value = "保存角色和菜单权限关系",notes = "保存角色和菜单权限关系")
    public AjaxResult saveRoleMenu(@PathVariable("roleId") Long roleId,@PathVariable("menuIds") Long[] menuIds){
        if(menuIds.length==1 && menuIds[0]==-1){
            //没有勾选
            menuIds= new Long[0];
        }
        this.roleservice.saveRoleMenu(roleId,menuIds);
        return AjaxResult.success("分配成功");
    }


    


}
