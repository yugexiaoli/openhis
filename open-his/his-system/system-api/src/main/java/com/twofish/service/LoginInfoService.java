package com.twofish.service;

import com.twofish.domain.LoginInfo;
import com.twofish.dto.LoginInfoDto;
import com.twofish.vo.DataGridView;

public interface LoginInfoService{
    /**
     * 插入登录日志
     * @param loginInfo
     * @return
     */
    int insertLoginIno(LoginInfo loginInfo);

    /**
     * 分页查询登录日志
     * @param loginInfoDto
     * @return
     */
    DataGridView listForPage(LoginInfoDto loginInfoDto);

    /**
     * 删除登录日志
     * @param infoIds
     * @return
     */
    int deleteLoginInfoByIds(Long[] infoIds);


    /**
     * 清空登录日志
     * @return
     */
    int clearLoginInfo();
}
