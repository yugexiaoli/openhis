package com.twofish.service;

import com.twofish.domain.OperLog;
import com.twofish.dto.OperLogDto;
import com.twofish.vo.DataGridView;

public interface OperLogService {

    /**
     * 插入操作日志
     * @param operLog
     */
    void insertOperLog(OperLog operLog);

    /**
     * 分页查询操作日志
     * @param operLogDto
     * @return
     */
    DataGridView listForPage(OperLogDto operLogDto);

    /**
     * 删除操作日志(可批量)
     * @param infoIds
     * @return
     */
    int deleteOperLogByIds(Long[] infoIds);


    /**
     * 清空所有操作日志
     * @return
     */
    int clearAllOperLog();
}
