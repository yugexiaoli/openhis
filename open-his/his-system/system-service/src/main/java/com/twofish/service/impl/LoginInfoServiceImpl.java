package com.twofish.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.domain.LoginInfo;
import com.twofish.mapper.LoginInfoMapper;
import com.twofish.service.LoginInfoService;
@Service
public class LoginInfoServiceImpl implements LoginInfoService{
    @Resource
    private LoginInfoMapper loginInfoMapper;

}
