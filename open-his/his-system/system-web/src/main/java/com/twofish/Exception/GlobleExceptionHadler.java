package com.twofish.Exception;

import com.twofish.vo.AjaxResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理类
 * @author ccy
 */
@RestControllerAdvice
@Log4j2
public class GlobleExceptionHadler {

    //private ObjectError allError;

    //未指定异常 所有异常处理根方法
    @ExceptionHandler(Exception.class)
    public AjaxResult exception(Exception e){
        return AjaxResult.error(e.getMessage());
    }


    //异常信息转化方法，用于将异常信息封装地更美观
    public List errMsgExchange(List<ObjectError> allErrors ){
        List<Map<String,Object>> list=new ArrayList<>();
        for (ObjectError allError : allErrors) {
            Map<String,Object> map=new HashMap<>();
            map.put("DefaultMessage",allError.getDefaultMessage());
            map.put("ObjectName",allError.getObjectName());
            FieldError fieldError=(FieldError)allError;
            map.put("Field", fieldError.getField());
            list.add(map);
        }
        return list;
    }

    //登录参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodargsexception(MethodArgumentNotValidException e){
        return  AjaxResult.fail("登录失败，参数异常",errMsgExchange(e.getBindingResult().getAllErrors()));
    }


    //字典类型参数异常处理 异常信息转化
    @ExceptionHandler(BindException.class)
    public AjaxResult exception(BindException e){
        return AjaxResult.fail("字典类型参数异常,操作失败",errMsgExchange(e.getBindingResult().getAllErrors()));
    }


    //方法参数类型异常 异常信息转化
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult exception(MethodArgumentTypeMismatchException e){
        Map<String,Object> map=new HashMap<>();
        map.put("errName",e.getName());
        map.put("errCauseMsg",e.getCause().getMessage());
        return AjaxResult.fail("方法参数异常,操作失败",map);
    }



}
