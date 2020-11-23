package com.twofish.controller.system;

import cn.hutool.core.date.DateUtil;
import com.twofish.constants.Constants;
import com.twofish.constants.HttpStatus;
import com.twofish.domain.LoginInfo;
import com.twofish.domain.Menu;
import com.twofish.domain.SimpleUser;
import com.twofish.domain.User;
import com.twofish.service.LoginInfoService;
import com.twofish.service.MenuService;
import com.twofish.service.UserService;
import com.twofish.utils.AddressUtils;
import com.twofish.utils.IpUtils;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.ActivierUser;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.LoginBodyDto;
import com.twofish.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private LoginInfoService loginInfoService;

    /**
     * 登录方法
     * @param loginBodyDto 前端登录vo对象
     * @return
     */
    @PostMapping("login/doLogin")
    @ApiOperation(value = "登录方法",notes = "登录")
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto,HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        AjaxResult ajax = AjaxResult.success();
        UsernamePasswordToken token = new UsernamePasswordToken(loginBodyDto.getUsername(),loginBodyDto.getPassword());
        LoginInfo loginInfo = this.createLoginInfo(request);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            Serializable webtoken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webtoken);
            //登录日志
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setMsg("登陆成功");
        }catch (Exception e){
            log.error("登录错误信息："+e.getMessage());
            ajax=AjaxResult.error(HttpStatus.ERROR,"用户名或密码错误");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
            loginInfo.setMsg("登陆失败，用户名或密码错误");
        }
        this.loginInfoService.insertLoginIno(loginInfo);
        return ajax;
    }


    /**
     * 人脸登录
     * @return
     *
     */
    @GetMapping("login/faceLogin/{userId}")
    @ApiOperation(value = "人脸登录",notes = "人脸登录")
    public AjaxResult faceLogin(@PathVariable Long userId, HttpServletRequest request){
        User user = userService.getone(userId);
        Subject subject = SecurityUtils.getSubject();
        AjaxResult ajax = AjaxResult.success();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getPhone(),user.getPhone().substring(5));
        LoginInfo loginInfo = this.createLoginInfo(request);
        loginInfo.setLoginAccount(user.getPhone());
        try {
            subject.login(token);
            Serializable webtoken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webtoken);
            //登录日志
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setMsg("登陆成功");
        }catch (Exception e){
            log.error("登录错误信息："+e.getMessage());
            ajax=AjaxResult.error(HttpStatus.ERROR,"用户名或密码错误");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
            loginInfo.setMsg("登陆失败，用户名或密码错误");
        }
        this.loginInfoService.insertLoginIno(loginInfo);
        return ajax;
    }



    /**
     * 得到用户的登陆信息
     * @param request
     * @return
     */
    private LoginInfo createLoginInfo(HttpServletRequest request) {
        LoginInfo loginInfo=new LoginInfo();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        loginInfo.setIpAddr(ip);
        loginInfo.setLoginLocation(address);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setLoginTime(DateUtil.date());
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginInfo;
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
