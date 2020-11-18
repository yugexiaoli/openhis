package com.twofish;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ccy
 * 系统启动类
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = {"com.twofish.mapper"})
@EnableDubbo
@Slf4j
public class SystemApplication {
     public static void main(String args[]) {
            initDatabase();
          SpringApplication.run(SystemApplication.class,args);
          log.info("主系统启动成功...");
      }
    public static void initDatabase() {
        String filename = "gen.db";
        String filepath = System.getProperty("user.dir") + "/" + filename;
        File dbFile = new File(filepath);
        if (!dbFile.exists()) {
            ClassPathResource resource = new ClassPathResource(filename);
            try {
                FileCopyUtils.copy(resource.getInputStream(), new FileOutputStream(dbFile));
            } catch (IOException e) {
                throw new RuntimeException("初始化数据库失败", e);
            }
        }
    }
}
