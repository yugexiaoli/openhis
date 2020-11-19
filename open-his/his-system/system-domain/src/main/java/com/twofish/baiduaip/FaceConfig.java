package com.twofish.baiduaip;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读yaml中百度人脸识别的配置信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "baiduaip")
public class FaceConfig {
    //设置APPID/AK/SK
    public String APP_ID;
    public String API_KEY;
    public String SECRET_KEY;

}
