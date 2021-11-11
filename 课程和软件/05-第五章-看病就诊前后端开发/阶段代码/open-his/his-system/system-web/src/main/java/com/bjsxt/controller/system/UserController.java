package com.bjsxt.controller.system;

import com.bjsxt.aspectj.annotation.Log;
import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.dto.UserDto;
import com.bjsxt.service.UserService;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import com.bjsxt.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Auther: 尚学堂 雷哥
 * @Description:用户管理控制器
 */
@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询
     */
    @GetMapping("listUserForPage")
    public AjaxResult listUserForPage(UserDto userDto){
        DataGridView gridView = this.userService.listUserForPage(userDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addUser")
    @Log(title = "添加用户",businessType = BusinessType.INSERT)
    public AjaxResult addUser(@Validated UserDto userDto) {
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userService.addUser(userDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateUser")
    @Log(title = "修改用户",businessType = BusinessType.UPDATE)
    public AjaxResult updateUser(@Validated UserDto userDto) {
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userService.updateUser(userDto));
    }


    /**
     * 根据ID查询一个用户信息
     */
    @GetMapping("getUserById/{userId}")
    public AjaxResult getUserById(@PathVariable @Validated @NotNull(message = "用户ID不能为空") Long userId) {
        return AjaxResult.success(this.userService.getOne(userId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteUserByIds/{userIds}")
    @Log(title = "删除用户",businessType = BusinessType.DELETE)
    public AjaxResult deleteUserByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] userIds) {
        return AjaxResult.toAjax(this.userService.deleteUserByIds(userIds));
    }

    /**
     * 查询所有可用的用户
     */
    @GetMapping("selectAllUser")
    public AjaxResult selectAllUser(){
        return AjaxResult.success(this.userService.getAllUsers());
    }
    /**
     * 重置密码
     */
    @PostMapping("resetPwd/{userIds}")
    public AjaxResult resetPwd(@PathVariable Long[] userIds){
        if(userIds.length>0){
            this.userService.resetPassWord(userIds);
            return AjaxResult.success("重置成功");
        }
        return AjaxResult.fail("重置失败,没有选择用户");

    }

}
