package com.twofish.service;

import com.twofish.baiduaip.RootResult;

public interface FaceService {

    /**
     *  人脸搜索
     * @param imgSrc
     * @return
     */
    RootResult search(String imgSrc);


}
