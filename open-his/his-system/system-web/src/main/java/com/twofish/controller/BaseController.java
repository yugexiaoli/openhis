package com.twofish.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.twofish.vo.AjaxResult;

@DefaultProperties(defaultFallback = "fallback")
public class BaseController {

    /**
     * 远程调用失败或者服务失效时进入这个容错保护方法
     * @return
     */
    public AjaxResult fallback(){
        return AjaxResult.error("服务器出现异常，请联系管理员");
    }
}
