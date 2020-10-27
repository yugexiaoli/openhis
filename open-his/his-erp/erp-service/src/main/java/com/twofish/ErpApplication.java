package com.twofish;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 进销存药品主入口
 * @author ccy
 *
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.twofish.mapper"})
@EnableDubbo
@Slf4j
public class ErpApplication {
     public static void main(String args[]) {
         SpringApplication.run(ErpApplication.class,args);
         log.info("erp子系统启动成功");
      } 
}
