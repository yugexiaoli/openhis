package com.twofish;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 统计分析系统主入口
 * @author ccy
 *
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.twofish.mapper"})
@EnableDubbo
@Slf4j
public class StatisticsApplication {
     public static void main(String args[]) {
         SpringApplication.run(StatisticsApplication.class,args);
         log.info("系统分析子系统启动成功。。");
      }
}
