package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.twofish.constants.Constants;
import com.twofish.domain.CareOrderItem;
import com.twofish.mapper.CareOrderItemMapper;
import com.twofish.service.CareOrderItemService;
import com.twofish.utils.IdGeneratorSnowflake;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 处方详情接口实现
 */
@Service
public class CareOrderItemServiceImpl  implements CareOrderItemService{
    @Resource
    private CareOrderItemMapper careOrderItemMapper;

    @Override
    public int deleteOrderItemByItemId(String itemId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_ITEM_ID,itemId);
        return this.careOrderItemMapper.delete(qw);
    }

    @Override
    public CareOrderItem queryOrderItemByItemId(String itemId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_ITEM_ID,itemId);
        return this.careOrderItemMapper.selectOne(qw);
    }

    @Override
    public void saveCareOrderItem(CareOrderItem careOrderItemDto) {
        CareOrderItem careOrderItem = new CareOrderItem();
        BeanUtil.copyProperties(careOrderItemDto,careOrderItem);
        careOrderItem.setCreateTime(DateUtil.date());
        careOrderItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_COI));
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
        this.careOrderItemMapper.insert(careOrderItem);
    }

    @Override
    public List<CareOrderItem> queryCoItemByCoId(String coId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID,coId);
        return this.careOrderItemMapper.selectList(qw);
    }
}
