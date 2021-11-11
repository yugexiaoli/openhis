package com.bjsxt.service;

import com.bjsxt.domain.Producter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.ProducterDto;
import com.bjsxt.vo.DataGridView;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface ProducterService {

    /**
     * 分页查询
     *
     * @param producterDto
     * @return
     */
    DataGridView listProducterPage(ProducterDto producterDto);

    /**
     * 根据ID查询
     *
     * @param producterId
     * @return
     */
    Producter getOne(Long producterId);

    /**
     * 添加
     *
     * @param producterDto
     * @return
     */
    int addProducter(ProducterDto producterDto);

    /**
     * 修改
     *
     * @param producterDto
     * @return
     */
    int updateProducter(ProducterDto producterDto);

    /**
     * 根据ID删除
     *
     * @param producterIds
     * @return
     */
    int deleteProducterByIds(Long[] producterIds);

    /**
     * 查询所有可用生产厂家
     */
    List<Producter> selectAllProducter();


}
