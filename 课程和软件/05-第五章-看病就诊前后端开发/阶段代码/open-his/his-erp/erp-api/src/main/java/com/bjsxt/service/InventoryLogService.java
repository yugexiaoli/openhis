package com.bjsxt.service;

import com.bjsxt.dto.InventoryLogDto;
import com.bjsxt.vo.DataGridView;

/**
 * @Author: 尚学堂 雷哥
 */
public interface InventoryLogService {
    /**
     * 分页查询
     *
     * @param inventoryLogDto
     * @return
     */
    DataGridView listInventoryLogPage(InventoryLogDto inventoryLogDto);
}
