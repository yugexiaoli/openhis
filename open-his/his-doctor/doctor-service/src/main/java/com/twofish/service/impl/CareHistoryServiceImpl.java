package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.constants.Constants;
import com.twofish.domain.CareHistory;
import com.twofish.dto.CareHistoryDto;
import com.twofish.mapper.CareHistoryMapper;
import com.twofish.service.CareHistoryService;
import com.twofish.utils.IdGeneratorSnowflake;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 病例接口实现
 */
@Service
public class CareHistoryServiceImpl  implements CareHistoryService{
    @Autowired
    private CareHistoryMapper careHistoryMapper;

    @Override
    public List<CareHistory> queryCareHistoryByPatientId(String patientId) {
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(CareHistory.COL_PATIENT_ID,patientId);
        qw.orderByDesc(CareHistory.COL_CARE_TIME);
        return this.careHistoryMapper.selectList(qw);
    }

    @Override
    public CareHistory updateHistory(CareHistoryDto careHistoryDto) {
        CareHistory careHistory = new CareHistory();
        BeanUtil.copyProperties(careHistoryDto,careHistory);
        careHistory.setCareTime(DateUtil.parse(careHistoryDto.getCareTime(),"yyyy-MM-dd HH:mm:ss"));
        this.careHistoryMapper.updateById(careHistory);
        return careHistory;
    }

    @Override
    public CareHistory addHistory(CareHistoryDto careHistoryDto) {
        CareHistory careHistory = new CareHistory();
        BeanUtil.copyProperties(careHistoryDto,careHistory);
        careHistory.setCareTime(DateUtil.parse(careHistoryDto.getCareTime(),"yyyy-MM-dd HH:mm:ss"));
        careHistory.setChId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CH));
        this.careHistoryMapper.insert(careHistory);
        return careHistory;
    }

    @Override
    public CareHistory getCareHistoryByRegId(String regId) {
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(regId),CareHistory.COL_REG_ID,regId);
        return this.careHistoryMapper.selectOne(qw);
    }

}
