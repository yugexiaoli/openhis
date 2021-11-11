package com.bjsxt.utils;

import com.bjsxt.constants.Constants;
import com.bjsxt.domain.SimpleUser;
import com.bjsxt.domain.User;
import com.bjsxt.vo.ActiverUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */
public class ShiroSecurityUtils {

    /***
     * @Description: 得到当前登陆的用户对象的ActiveUser
     */
    public static ActiverUser getCurrentActiveUser(){
        Subject subject= SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        return activerUser;
    }

    /***
     * @Description: 得到当前登陆的用户对象User
     */
    public static User getCurrentUser(){
        return getCurrentActiveUser().getUser();
    }

    /***
     * @Description: 得到当前登陆的用户对象SimpleUser
     */
    public static SimpleUser getCurrentSimpleUser(){
        User user = getCurrentActiveUser().getUser();
        return new SimpleUser(user.getUserId(),user.getUserName());
    }

    /***
     * @Description: 得到当前登陆的用户名称
     */
    public static String getCurrentUserName(){
        return getCurrentActiveUser().getUser().getUserName();
    }

    /***
     * @Description: 得到当前登陆对象的角色编码
     */
    public static List<String> getCurrentUserRoles(){
        return getCurrentActiveUser().getRoles();
    }


    /***
     * @Description: 得到当前登陆对象的权限编码
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiveUser().getPermissions();
    }

    /***
     * @Description: 判断当前用户是否是超管
     */
    public static boolean isAdmin(){
        return getCurrentUser().getUserType().equals(Constants.USER_ADMIN);
    }
}
