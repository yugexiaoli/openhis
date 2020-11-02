package com.twofish.controller.system;

import com.twofish.vo.ActivierUser;
import com.twofish.constants.Constants;
import com.twofish.constants.HttpStatus;
import com.twofish.domain.Menu;
import com.twofish.domain.SimpleUser;
import com.twofish.service.MenuService;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.LoginBodyDto;
import com.twofish.vo.MenuTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 登录接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "系统登录接口",tags = "登录接口")
public class LoginController {

    @Autowired
    private MenuService menuService;

    /**
     * 登录方法
     * @param loginBodyDto 前端登录vo对象
     * @return
     */
    @PostMapping("login/doLogin")
    @ApiOperation(value = "登录方法",notes = "登录")
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto){
        Subject subject = SecurityUtils.getSubject();
        AjaxResult ajax = AjaxResult.success();
        UsernamePasswordToken token = new UsernamePasswordToken(loginBodyDto.getUsername(),loginBodyDto.getPassword());
        try {
            subject.login(token);
            Serializable webtoken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webtoken);
        }catch (Exception e){
            log.error("登录错误信息："+e.getMessage());
            ajax=AjaxResult.error(HttpStatus.ERROR,"用户名或密码错误");
        }
        return ajax;
    }

    /**
     * 登录后获取用户信息
     * @return
     */
    @GetMapping("login/getInfo")
    @ApiOperation(value = "登录后获取用户信息",notes = "获取用户信息")
    public AjaxResult getInfo(){
        AjaxResult ajax = AjaxResult.success();
        Subject subject = SecurityUtils.getSubject();
        ActivierUser activierUser =(ActivierUser)subject.getPrincipal();
        System.out.println("activierUser"+activierUser);
        ajax.put("username",activierUser.getUser().getUserName());
        ajax.put("picture",activierUser.getUser().getPicture());
        ajax.put("roles",activierUser.getRoles());
        ajax.put("permissions",activierUser.getPermissions());
        return ajax;
    }

    /**
     * 用户退出接口
     * @return
     */
    @GetMapping("login/logout")
    @ApiOperation(value = "用户退出",notes = "用户退出")
    public AjaxResult logout(){
        AjaxResult ajax = AjaxResult.success("用户退出成功");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ajax;
    }

    /**
     * 根据用户角色权限获取菜单
     * @return
     */
    @GetMapping("login/getMenus")
    @ApiOperation(value = "获取菜单",notes = "获取菜单")
    public AjaxResult getMenus(){
        AjaxResult ajax = AjaxResult.success();
        Subject subject = SecurityUtils.getSubject();
        ActivierUser activierUser =(ActivierUser)subject.getPrincipal();
        boolean isAdmin = activierUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        SimpleUser simpleUser=null;
        if(!isAdmin){
            //系统管理员 才用的到simpleUser
            simpleUser=new SimpleUser(activierUser.getUser().getUserId(), activierUser.getUser().getUserName());
        }
        List<Menu> menus = menuService.selectMenuTree(isAdmin,simpleUser);
        List<MenuTreeVo> menuTreeVos = new ArrayList<>();
        for (Menu menu : menus) {
            menuTreeVos.add(new MenuTreeVo(menu.getMenuId().toString(),menu.getPath()));
        }
        ajax.put("menuTreeVos",menuTreeVos);
        return ajax;
    }



}
