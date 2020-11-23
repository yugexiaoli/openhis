package com.twofish.baiduaip;

import lombok.Data;

@Data
public class RootResult
{
    public String error_code;
    public String error_msg;
    public Long log_id ;
    public String timestamp ;
    public String cached ;
    public FaceResult result ;
}