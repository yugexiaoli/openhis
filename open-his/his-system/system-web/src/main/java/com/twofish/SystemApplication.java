package com.twofish;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ccy
 * 系统启动类
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.twofish.mapper"})
@EnableDubbo
@Slf4j
public class SystemApplication {
     public static void main(String args[]) {
          SpringApplication.run(SystemApplication.class,args);
          log.info("主系统启动成功...");
      }
}
