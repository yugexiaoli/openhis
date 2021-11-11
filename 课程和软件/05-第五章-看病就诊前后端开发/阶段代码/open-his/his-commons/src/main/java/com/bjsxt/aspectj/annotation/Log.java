package com.bjsxt.aspectj.annotation;

import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.aspectj.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @Author: 尚学堂 雷哥
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
