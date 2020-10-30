package com.twofish.Exception;

import com.twofish.vo.AjaxResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    private ObjectError allError;

    //登录参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodargsexception(MethodArgumentNotValidException e){

        List<Map<String,Object>> list=new ArrayList<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        for (ObjectError allError : allErrors) {
            Map<String,Object> map=new HashMap<>();

            map.put("DefaultMessage",allError.getDefaultMessage());
            map.put("ObjectName",allError.getObjectName());
            FieldError fieldError=(FieldError)allError;
            map.put("Field", fieldError.getField());
            list.add(map);
        }
        return  AjaxResult.fail("登录失败，参数异常",list);
    }
}
