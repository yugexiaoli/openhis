package com.bjsxt.controller.system;

import cn.hutool.core.date.DateUtil;
import com.bjsxt.aspectj.annotation.Log;
import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.constants.Constants;
import com.bjsxt.constants.HttpStatus;
import com.bjsxt.domain.LoginInfo;
import com.bjsxt.domain.Menu;
import com.bjsxt.domain.SimpleUser;
import com.bjsxt.dto.LoginBodyDto;
import com.bjsxt.service.LoginInfoService;
import com.bjsxt.service.MenuService;
import com.bjsxt.utils.AddressUtils;
import com.bjsxt.utils.IpUtils;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.ActiverUser;
import com.bjsxt.vo.AjaxResult;
import com.bjsxt.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
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

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */
@RestController
@Log4j2
public class LoginController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginInfoService loginInfoService;

    /**
     * 登录方法
     *
     * @return 结果
     */
    @PostMapping("login/doLogin")
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        //构造用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        //封装LoginInfo
        LoginInfo loginInfo=createLoginInfo(request);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            //得到会话的token==也就是redis里面存的
            Serializable webToken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webToken);

            //说明登陆成功
            loginInfo.setMsg("登陆成功");
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
        } catch (Exception e) {
            log.error("用户名或密码不正确", e);
            ajax = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            loginInfo.setMsg("用户名或密码不正确");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
        }
        //保存登陆信息到数据库
        loginInfoService.insertLoginInfo(loginInfo);
        return ajax;
    }

    /**
     * 构造LoginInfo
     * @param request
     * @return
     */
    private LoginInfo createLoginInfo(HttpServletRequest request) {
        LoginInfo loginInfo=new LoginInfo();
        UserAgent userAgent=UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //获取ID地址
        String ipAddr= IpUtils.getIpAddr(request);
        //获取操作系统
        String os=userAgent.getOperatingSystem().getName();
        //获取浏览器类型
        String browser=userAgent.getBrowser().getName();
        //获取登陆地址
        String location= AddressUtils.getRealAddressByIP(ipAddr);

        loginInfo.setIpAddr(ipAddr);
        loginInfo.setLoginLocation(location);
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setLoginTime(DateUtil.date());
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginInfo;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("login/getInfo")
    public AjaxResult getInfo()
    {
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("username", activerUser.getUser().getUserName());
        ajax.put("picture", activerUser.getUser().getPicture());
        ajax.put("roles", activerUser.getRoles());
        ajax.put("permissions", activerUser.getPermissions());
        return ajax;
    }

    /**
     * 用户退出
     */
    @PostMapping("login/logout")
    @Log(title = "用户退出",businessType = BusinessType.OTHER)
    public AjaxResult logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    /**
     * 获取应该显示的菜单信息
     *
     * @return 菜单信息
     */
    @GetMapping("login/getMenus")
    public AjaxResult getMeuns()
    {
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        boolean isAdmin=activerUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        SimpleUser simpleUser=null;
        if(!isAdmin){
            simpleUser=new SimpleUser(activerUser.getUser().getUserId(),activerUser.getUser().getUserName());
        }
        List<Menu> menus = menuService.selectMenuTree(isAdmin,simpleUser);
        List<MenuTreeVo> menuVos=new ArrayList<>();
        for (Menu menu : menus) {
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(),menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }
}
