package com.twofish.service;

import com.twofish.domain.RegisteredItem;
import com.twofish.dto.RegisteredItemDto;
import com.twofish.vo.DataGridView;

public interface RegisteredItemService {

    /**
     * 分页查询所有
     * @param registeredItemDto
     * @return
     */
    DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto);

    /**
     * 添加
     * @param registeredItemDto
     * @return
     */
    int addRegisteredItem(RegisteredItemDto registeredItemDto);

    /**
     * 根据id查询
     * @param registeredItemId
     * @return
     */
    RegisteredItem getRegisteredItemById(Long registeredItemId);

    /**
     * 修改
     * @param registeredItemDto
     * @return
     */
    int updateRegisteredItem(RegisteredItemDto registeredItemDto);

    /**
     * 删除(可批量)
     * @param registeredItemIds
     * @return
     */
    int deleteRegisteredItemByIds(Long[] registeredItemIds);

    /**
     * 查询所有可用的
     * @return
     */
    DataGridView selectAllRegisteredItem();

}
