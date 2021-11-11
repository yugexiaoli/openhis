package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.dto.OperLogDto;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.OperLog;
import com.bjsxt.mapper.OperLogMapper;
import com.bjsxt.service.OperLogService;
/**
* @Author: 尚学堂 雷哥
*/

@Service
public class OperLogServiceImpl  implements OperLogService{

    @Autowired
    private OperLogMapper operLogMapper;

    @Override
    public void insertOperLog(OperLog operLog) {
        operLogMapper.insert(operLog);
    }

    @Override
    public DataGridView listForPage(OperLogDto operLogDto) {
        Page<OperLog> page = new Page<>(operLogDto.getPageNum(), operLogDto.getPageSize());
        QueryWrapper<OperLog> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(operLogDto.getOperName()), OperLog.COL_OPER_NAME, operLogDto.getOperName());
        qw.like(StringUtils.isNotBlank(operLogDto.getTitle()), OperLog.COL_TITLE, operLogDto.getTitle());
        qw.eq(StringUtils.isNotBlank(operLogDto.getBusinessType()), OperLog.COL_BUSINESS_TYPE, operLogDto.getBusinessType());
        qw.eq(StringUtils.isNotBlank(operLogDto.getStatus()), OperLog.COL_STATUS, operLogDto.getStatus());
        qw.ge(null != operLogDto.getBeginTime(), OperLog.COL_OPER_TIME, operLogDto.getBeginTime());
        qw.le(null != operLogDto.getEndTime(), OperLog.COL_OPER_TIME, operLogDto.getEndTime());
        qw.orderByDesc(OperLog.COL_OPER_TIME);
        this.operLogMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int deleteOperLogByIds(Long[] infoIds) {
        if (null != infoIds && infoIds.length > 0) {
            return this.operLogMapper.deleteBatchIds(Arrays.asList(infoIds));
        }
        return 0;
    }

    @Override
    public int clearAllOperLog() {
        return this.operLogMapper.delete(null);
    }
}
