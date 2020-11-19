package com.twofish.service.impl;

import com.baidu.aip.face.AipFace;
import com.twofish.baiduaip.FaceConfig;
import com.twofish.service.FaceService;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.HashMap;

@Service
@EnableConfigurationProperties(value = {FaceConfig.class})
public class FaceServiceImpl implements FaceService, InitializingBean {

    //设置APPID/AK/SK
    public String APP_ID ;
    public String API_KEY ;
    public String SECRET_KEY ;

    @Resource
    private FaceConfig faceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.API_KEY=faceConfig.API_KEY;
        this.APP_ID=faceConfig.APP_ID;
        this.SECRET_KEY=faceConfig.SECRET_KEY;
    }

    @Override
    public JSONObject search(String imgSrc) {

        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
        String imageType = "BASE64";
        String groupIdList = "id_1";
        // 传入可选参数调用接口
        HashMap options = new HashMap();
        options.put("max_face_num", "3");
        options.put("match_threshold", "70");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("user_id", "user1");
        options.put("max_user_num", "3");

        //encode解码
        String decodeSrc = URLDecoder.decode(imgSrc);
        if(decodeSrc.endsWith("=")){
            decodeSrc= decodeSrc.substring(0, decodeSrc.lastIndexOf("="));
        }

        JSONObject res = client.search(decodeSrc, imageType,groupIdList, options);
        //后台res中result中有一个face_token,需要存redis中，并且让shiro知道
        
        return res;
    }




}
