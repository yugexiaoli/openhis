package com.twofish.service;

import org.json.JSONObject;

public interface FaceService {

    /**
     *  人脸搜索
     * @param imgSrc
     * @return
     */
    JSONObject search(String imgSrc);


}
