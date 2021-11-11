package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.domain.CareOrder;
import com.twofish.mapper.CareOrderMapper;
import com.twofish.service.CareOrderService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 处方接口实现
 */
@Service
public class CareOrderServiceImpl implements CareOrderService{
    @Resource
    private CareOrderMapper careOrderMapper;


    @Override
    public void saveCareOrder(CareOrder careOrderDto) {
        CareOrder careOrder = new CareOrder();
        BeanUtil.copyProperties(careOrderDto,careOrder);
        careOrder.setCreateTime(DateUtil.date());
        this.careOrderMapper.insert(careOrder);
    }

    @Override
    public List<CareOrder> queryCoBychId(String chId) {
        QueryWrapper<CareOrder> qw = new QueryWrapper<>();
        qw.eq(CareOrder.COL_CH_ID,chId);
        return this.careOrderMapper.selectList(qw);
    }
}
