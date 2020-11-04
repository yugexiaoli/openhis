package com.twofish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.dto.OperLogDto;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.mapper.OperLogMapper;
import com.twofish.domain.OperLog;
import com.twofish.service.OperLogService;
@Service
public class OperLogServiceImpl  implements OperLogService{
    @Resource
    private OperLogMapper operLogMapper;

    @Override
    public void insertOperLog(OperLog operLog) {
        this.operLogMapper.insert(operLog);
    }

    @Override
    public DataGridView listForPage(OperLogDto operLogDto) {
        Page<OperLog> page = new Page<>(operLogDto.getPageNum(),operLogDto.getPageSize());
        QueryWrapper<OperLog> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(operLogDto.getTitle()),OperLog.COL_TITLE,operLogDto.getTitle());
        qw.eq(StringUtils.isNotBlank(operLogDto.getBusinessType()),OperLog.COL_BUSINESS_TYPE,operLogDto.getBusinessType());
        qw.like(StringUtils.isNotBlank(operLogDto.getOperName()),OperLog.COL_OPER_NAME,operLogDto.getOperName());
        qw.eq(StringUtils.isNotBlank(operLogDto.getStatus()),OperLog.COL_STATUS,operLogDto.getStatus());
        qw.ge(null!=operLogDto.getBeginTime(),OperLog.COL_OPER_TIME,operLogDto.getBeginTime());
        qw.le(null!=operLogDto.getEndTime(),OperLog.COL_OPER_TIME,operLogDto.getEndTime());
        qw.orderByDesc(OperLog.COL_OPER_TIME);
        this.operLogMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteOperLogByIds(Long[] infoIds) {
        List<Long> ids = Arrays.asList(infoIds);
        if(ids!=null && ids.size()>0){
            return this.operLogMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public int clearAllOperLog() {
         return this.operLogMapper.delete(null);
    }
}
