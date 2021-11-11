package com.twofish.service;

import com.twofish.domain.CareOrderItem;

import java.util.List;

public interface CareOrderItemService {

    //根据处方id查询处方详情列表
    List<CareOrderItem> queryCoItemByCoId(String coId);
    //添加处方详情
    void saveCareOrderItem(CareOrderItem careOrderItemDto);

    //根据id查询处方详情
    CareOrderItem queryOrderItemByItemId(String itemId);
    //删除处方详情
    int deleteOrderItemByItemId(String itemId);
}
