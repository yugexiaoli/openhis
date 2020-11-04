package com.twofish.aspectj;

import com.twofish.service.OperLogService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.logging.Handler;

/**
 * 记录操作日志切面类
 * @author ccy
 */
@Aspect
@Component
@Log4j2
public class OperLogAspect {

    @Resource
    private OperLogService operLogService;


    //配置织入点
    @Pointcut("@annotation(com.twofish.aspectj.annotation.Log)")
    private void logPointCut() {
    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning("logPointCut()")
    public void afterReturning() {
        System.out.println("======后置通知后置通知=======");
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing("logPointCut()")
    public void afterThrowing() {

    }

}
