package com.twofish;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 就诊子系统主入口
 * @author ccy
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.twofish.mapper"})
@EnableDubbo
@Slf4j
public class DoctorApplication {
     public static void main(String args[]) {
         SpringApplication.run(DoctorApplication.class,args);
         log.info("就诊子系统启动成功。。。");
      }
}
