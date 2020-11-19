package com.twofish.service;

import com.twofish.domain.CheckItem;
import com.twofish.dto.CheckItemDto;
import com.twofish.vo.DataGridView;

public interface CheckItemService {

    /**
     * 分页查询所有检查费用表
     * @param checkItemDto
     * @return
     */
    DataGridView listCheckItemForPage(CheckItemDto checkItemDto);

    /**
     * 添加检查费用表
     * @param checkItemDto
     * @return
     */
    int addCheckItem(CheckItemDto checkItemDto);

    /**
     * 根据id查询检查费用表
     * @param sysCheckItemId
     * @return
     */
    CheckItem getCheckItemById(Long sysCheckItemId);

    /**
     * 修改检查费用表
     * @param checkItemDto
     * @return
     */
    int updateCheckItem(CheckItemDto checkItemDto);

    /**
     * 删除检查费用表(可批量)
     * @param checkItemIds
     * @return
     */
    int deleteCheckItemByIds(Long[] checkItemIds);

    /**
     * 查询所有可用的检查费用表
     * @return
     */
    DataGridView selectAllCheckItem();
}
