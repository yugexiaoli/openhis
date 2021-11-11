package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.UserDto;
import com.twofish.service.UserService;
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
 * 用户接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "用户接口",tags = "用户接口")
@RequestMapping("/system/user/")
public class UserController {
    @Resource
    private UserService userservice;

    /**
     * 分页查询用户信息
     * @param userdto
     * @return
     */
    @GetMapping("listUserForPage")
    @ApiOperation(value = "分页查询用户信息",notes = "分页查询用户信息")
    public AjaxResult listUserForPage(UserDto userdto){
        DataGridView dataGridView = this.userservice.listUserForPage(userdto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 查询所有可用的系统用户
     *
     * @return
     */
    @GetMapping("selectAllUser")
    @ApiOperation(value = "查询所有可用用户",notes = "查询所有可用用户")
    public AjaxResult selectAllUser(){
        return AjaxResult.success("查询成功",this.userservice.selectAllUser());
    }

    /**
     * 添加用户
     * @param userdto
     * @return
     */
    @Log(title = "添加用户",businessType = BusinessType.INSERT)
    @PostMapping("addUser")
    @ApiOperation(value = "添加用户",notes = "添加用户")
    public AjaxResult addUser(@Validated UserDto userdto){
        if(userservice.checkphonehasexit(userdto.getPhone(),0L)){
            return AjaxResult.fail("手机号已存在");
        }
        //设置添加人
        userdto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userservice.addUser(userdto));
    }


    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
//    @Scheduled(cron = "{ }")
    @GetMapping("getUserById/{userId}")
    @ApiOperation(value = "根据用户ID查询用户信息",notes = "根据用户ID查询用户信息")
    public AjaxResult getUserById(@PathVariable Long userId){
        return AjaxResult.success("查询成功",this.userservice.getone(userId));
    }

    /**
     * 修改用户信息
     * @param userdto
     * @return
     */
    @Log(title = "修改用户信息",businessType = BusinessType.UPDATE)
    @PutMapping("updateUser")
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息")
    public AjaxResult updateUser(@Validated UserDto userdto){
        if(userservice.checkphonehasexit(userdto.getPhone(),userdto.getUserId())){
            return AjaxResult.fail("手机号已存在");
        }
        //设置修改人
        userdto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userservice.updateUser(userdto));
    }


    /**
     * 根据ID删除用户信息(可批量删除)
     * @param userIds 用户id数组
     * @return
     */
    @Log(title = "根据ID删除用户信息",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteUserByIds/{userIds}")
    @ApiOperation(value = "根据ID删除用户信息",notes = "根据ID删除用户信息")
    public AjaxResult deleteUserByIds(@PathVariable Long[] userIds){
        return AjaxResult.toAjax(this.userservice.deleteUserByIds(userIds));
    }


    /**
     * 重置用户密码
     * @param userIds
     * @return
     */
    @Log(title = "重置用户密码",businessType = BusinessType.UPDATE)
    @PutMapping("resetPwd/{userIds}")
    @ApiOperation(value = "重置用户密码",notes = "重置用户密码")
    public AjaxResult resetPwd(@PathVariable Long[] userIds){
        this.userservice.resetPwd(userIds);
        return AjaxResult.success("重置成功");
    }

    /**
     * 根据用户ID查询角色IDS
     * @param userId
     * @return
     */
    @GetMapping("getRoleIdsByUserId/{userId}")
    @ApiOperation(value = "根据用户ID查询角色IDS",tags = "根据用户ID查询角色IDS")
    public AjaxResult getRoleIdsByUserId(@PathVariable Long userId){
        return AjaxResult.success("查询成功",this.userservice.getRoleIdsByUserId(userId));
    }

    /**
     * 保存用户和角色之间的关系
     * @param userId
     * @param roleIds
     * @return
     */
    @Log(title = "保存用户和角色之间的关系",businessType = BusinessType.INSERT)
    @PostMapping("saveUserRole/{userId}/{roleIds}")
    @ApiOperation(value = "保存用户和角色之间的关系",notes = "保存用户和角色之间的关系")
    public AjaxResult saveUserRole(@PathVariable("userId") Long userId, @PathVariable("roleIds") Long[] roleIds){
        if(roleIds.length==1 && roleIds[0]==-1){
            //没有勾选
            roleIds= new Long[0];
        }
        this.userservice.saveUserRole(userId,roleIds);
        return AjaxResult.success("分配成功");
    }

    /**
     * 查询需要排班的医生信息
     *
     * @return
     */
    @GetMapping("getUsersNeedScheduling")
    @ApiOperation(value = "查询需要排班的医生信息",tags = "查询需要排班的医生信息")
    public AjaxResult getUsersNeedScheduling(){
        return AjaxResult.success("查询成功",this.userservice.getUsersNeedScheduling());
    }

}
