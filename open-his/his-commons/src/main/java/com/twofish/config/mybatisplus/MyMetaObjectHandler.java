package com.twofish.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mp自动填充时间
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
//        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
//        this.strictInsertFill(metaObject, "updateTime",Date.class, new Date());
//        this.strictInsertFill(metaObject, "loginTime",Date.class, new Date());
        this.fillStrategy(metaObject, "createTime", new Date());
        this.fillStrategy(metaObject, "updateTime", new Date());
        this.fillStrategy(metaObject, "loginTime", new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.fillStrategy(metaObject, "updateTime", new Date());
//        this.strictUpdateFill(metaObject, "updateTime",Date.class, new Date());
    }
}