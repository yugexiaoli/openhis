package com.bjsxt;

import com.baomidou.mybatisplus.extension.api.R;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 尚学堂 雷哥
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.bjsxt.mapper"})
@EnableDubbo
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class,args);
        System.out.println("就诊子系统启动成功");
    }
}
