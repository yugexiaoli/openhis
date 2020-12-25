package com.twofish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.domain.InventoryLog;
import com.twofish.dto.InventoryLogDto;
import com.twofish.mapper.InventoryLogMapper;
import com.twofish.service.InventoryLogService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

@Service
public class InventoryLogServiceImpl  implements InventoryLogService{
    @Resource
    private InventoryLogMapper inventoryLogMapper;

    @Override
    public DataGridView listStockInventoryLogForPage(InventoryLogDto inventoryLogDto) {
        Page<InventoryLog> page = new Page<>(inventoryLogDto.getPageNum(),inventoryLogDto.getPageSize());
        QueryWrapper<InventoryLog> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getPurchaseId()),InventoryLog.COL_PURCHASE_ID,inventoryLogDto.getPurchaseId());
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getMedicinesName()),InventoryLog.COL_MEDICINES_NAME,inventoryLogDto.getMedicinesName());
        qw.eq(null!=inventoryLogDto.getMedicinesType(),InventoryLog.COL_MEDICINES_TYPE,inventoryLogDto.getMedicinesType());
        qw.eq(null!=inventoryLogDto.getProducterId(),InventoryLog.COL_PRODUCTER_ID,inventoryLogDto.getProducterId());
        qw.eq(null!=inventoryLogDto.getPrescriptionType(),InventoryLog.COL_PRESCRIPTION_TYPE,inventoryLogDto.getPrescriptionType());
        qw.ge(null!=inventoryLogDto.getBeginTime(),InventoryLog.COL_CREATE_TIME,inventoryLogDto.getBeginTime());
        qw.le(null!=inventoryLogDto.getEndTime(),InventoryLog.COL_CREATE_TIME,inventoryLogDto.getEndTime());
        qw.orderByDesc(InventoryLog.COL_CREATE_TIME);
        inventoryLogMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
}
