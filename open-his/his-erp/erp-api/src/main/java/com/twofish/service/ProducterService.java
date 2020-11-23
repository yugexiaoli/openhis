package com.twofish.service;

import com.twofish.domain.Producter;
import com.twofish.dto.ProducterDto;
import com.twofish.vo.DataGridView;

public interface ProducterService{
    /**
     * 分页查询所有生产厂家表
     * @param producterDto
     * @return
     */
    DataGridView listProducterForPage(ProducterDto producterDto);

    /**
     * 添加生产厂家表
     * @param producterDto
     * @return
     */
    int addProducter(ProducterDto producterDto);

    /**
     * 根据id查询生产厂家表
     * @param stockProducterId
     * @return
     */
    Producter getProducterById(Long stockProducterId);

    /**
     * 修改生产厂家表
     * @param producterDto
     * @return
     */
    int updateProducter(ProducterDto producterDto);

    /**
     * 删除生产厂家表(可批量)
     * @param stockProducterIds
     * @return
     */
    int deleteProducterByIds(Long[] stockProducterIds);

    /**
     * 查询所有可用的生产厂家表
     * @return
     */
    DataGridView selectAllProducter();

}
