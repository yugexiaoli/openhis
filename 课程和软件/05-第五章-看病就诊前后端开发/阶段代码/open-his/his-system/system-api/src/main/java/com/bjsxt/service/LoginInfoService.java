package com.bjsxt.service;

import com.bjsxt.domain.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.LoginInfoDto;
import com.bjsxt.vo.DataGridView;

/**
 * @Author: 尚学堂 雷哥
 */

public interface LoginInfoService {
    /**
     * 添加
     *
     * @param loginInfo
     * @return
     */
    int insertLoginInfo(LoginInfo loginInfo);

    /**
     * 分页查询
     *
     * @param loginInfoDto
     * @return
     */
    DataGridView listForPage(LoginInfoDto loginInfoDto);

    /**
     * 删除登陆日志
     *
     * @param infoIds
     * @return
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空登陆日志
     *
     * @return
     */
    int clearLoginInfo();

}
