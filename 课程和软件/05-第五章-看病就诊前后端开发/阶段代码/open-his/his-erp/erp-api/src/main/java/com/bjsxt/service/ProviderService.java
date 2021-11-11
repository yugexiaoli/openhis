package com.bjsxt.service;

import com.bjsxt.domain.Provider;
import com.bjsxt.domain.Provider;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.ProviderDto;
import com.bjsxt.vo.DataGridView;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface ProviderService {
    /**
     * 分页查询
     *
     * @param providerDto
     * @return
     */
    DataGridView listProviderPage(ProviderDto providerDto);

    /**
     * 根据ID查询
     *
     * @param providerId
     * @return
     */
    Provider getOne(Long providerId);

    /**
     * 添加
     *
     * @param providerDto
     * @return
     */
    int addProvider(ProviderDto providerDto);

    /**
     * 修改
     *
     * @param providerDto
     * @return
     */
    int updateProvider(ProviderDto providerDto);

    /**
     * 根据ID删除
     *
     * @param providerIds
     * @return
     */
    int deleteProviderByIds(Long[] providerIds);

    /**
     * 查询所有可用供应商
     */
    List<Provider> selectAllProvider();

}
