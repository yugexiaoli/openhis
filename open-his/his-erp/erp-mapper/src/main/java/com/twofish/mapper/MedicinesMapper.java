package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.Medicines;
import org.apache.ibatis.annotations.Param;

public interface MedicinesMapper extends BaseMapper<Medicines> {
    /**
     * 根据药品id和库存num修改药品信息
     * @param medicinesId
     * @param medicinesStockNum
     * @return
     */
    int updateMedicinesByIdAndNum(@Param("medicinesId") Long medicinesId,@Param("medicinesStockNum") Long medicinesStockNum);
}