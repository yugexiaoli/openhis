package com.twofish.service;

import com.twofish.domain.CareOrder;

import java.util.List;

public interface CareOrderService{

    //根据病例id查询处方列表
    List<CareOrder> queryCoBychId(String chId);
    //添加处方
    void saveCareOrder(CareOrder careOrderDto);
}
