package com.twofish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.dto.LoginInfoDto;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.domain.LoginInfo;
import com.twofish.mapper.LoginInfoMapper;
import com.twofish.service.LoginInfoService;
@Service
public class LoginInfoServiceImpl implements LoginInfoService{
    @Resource
    private LoginInfoMapper loginInfoMapper;

    @Override
    public int insertLoginIno(LoginInfo loginInfo) {
        return this.loginInfoMapper.insert(loginInfo);
    }

    @Override
    public DataGridView listForPage(LoginInfoDto loginInfoDto) {
        Page<LoginInfo> page = new Page<>(loginInfoDto.getPageNum(),loginInfoDto.getPageSize());
        QueryWrapper<LoginInfo> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(loginInfoDto.getUserName()),LoginInfo.COL_USER_NAME,loginInfoDto.getUserName());
        qw.like(StringUtils.isNotBlank(loginInfoDto.getLoginAccount()),LoginInfo.COL_LOGIN_ACCOUNT,loginInfoDto.getLoginAccount());
        qw.like(StringUtils.isNotBlank(loginInfoDto.getIpAddr()),LoginInfo.COL_IP_ADDR,loginInfoDto.getIpAddr());
        qw.eq(null!=loginInfoDto.getLoginStatus(),LoginInfo.COL_LOGIN_STATUS,loginInfoDto.getLoginStatus());
        qw.eq(null!=loginInfoDto.getLoginType(),LoginInfo.COL_LOGIN_TYPE,loginInfoDto.getLoginType());
        qw.ge(null!=loginInfoDto.getBeginTime(),LoginInfo.COL_LOGIN_TIME,loginInfoDto.getBeginTime());
        qw.le(null!=loginInfoDto.getEndTime(),LoginInfo.COL_LOGIN_TIME,loginInfoDto.getEndTime());
        qw.orderByDesc(LoginInfo.COL_LOGIN_TIME);
        loginInfoMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        List<Long> ids = Arrays.asList(infoIds);
        if(ids!=null && ids.size()>0){
            return this.loginInfoMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public int clearLoginInfo() {
        return this.loginInfoMapper.delete(null);
    }
}
