package com.twofish.service;

import com.twofish.domain.Provider;
import com.twofish.dto.ProviderDto;
import com.twofish.vo.DataGridView;

public interface ProviderService {

    /**
     * 分页查询所有供应商信息表
     * @param providerDto
     * @return
     */
    DataGridView listProviderForPage(ProviderDto providerDto);

    /**
     * 添加供应商信息表
     * @param providerDto
     * @return
     */
    int addProvider(ProviderDto providerDto);

    /**
     * 根据id查询供应商信息表
     * @param providerId
     * @return
     */
    Provider getProviderById(Long providerId);

    /**
     * 修改供应商信息表
     * @param providerDto
     * @return
     */
    int updateProvider(ProviderDto providerDto);

    /**
     * 删除供应商信息表(可批量)
     * @param providerIds
     * @return
     */
    int deleteProviderByIds(Long[] providerIds);

    /**
     * 查询所有可用的供应商信息表
     * @return
     */
    DataGridView selectAllProvider();
}
