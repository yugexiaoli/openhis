package com.twofish.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登录时前台传给后台的请求体，也就是登录表单vo对象
 * @author ccy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBodyDto  {
    //  用户名
    @NotNull(message = "用户名不能为空")
    private String username;
    //  密码
    @NotNull(message = "密码不能为空")
    private String password;
    // 验证码
    private String code;

}
