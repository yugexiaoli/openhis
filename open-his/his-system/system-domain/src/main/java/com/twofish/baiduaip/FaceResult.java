package com.twofish.baiduaip;

import lombok.Data;

import java.util.List;

@Data
public  class FaceResult
{
    public String face_token;
    public List<FaceList> user_list ;
}