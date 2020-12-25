package com.twofish.service;

import com.twofish.dto.InventoryLogDto;
import com.twofish.vo.DataGridView;

public interface InventoryLogService {
    /**
     * 分页查询所有
     * @param inventoryLogDto
     * @return
     */
    DataGridView listStockInventoryLogForPage(InventoryLogDto inventoryLogDto);
}
