package com.bjsxt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.RegisteredItem;
import com.bjsxt.dto.RegisteredItemDto;
import com.bjsxt.mapper.RegisteredItemMapper;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.RegisteredItem;
import com.bjsxt.mapper.RegisteredItemMapper;
import com.bjsxt.service.RegisteredItemService;
/**
* @Author: 尚学堂 雷哥
*/

@Service
public class RegisteredItemServiceImpl  implements RegisteredItemService{
    @Autowired
    private RegisteredItemMapper registeredItemMapper;

    @Override
    public DataGridView listRegisteredItemPage(RegisteredItemDto registeredItemDto) {
        Page<RegisteredItem> page=new Page<>(registeredItemDto.getPageNum(),registeredItemDto.getPageSize());
        QueryWrapper<RegisteredItem> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(registeredItemDto.getRegItemName()),RegisteredItem.COL_REG_ITEM_NAME,registeredItemDto.getRegItemName());
        qw.eq(StringUtils.isNotBlank(registeredItemDto.getStatus()),RegisteredItem.COL_STATUS,registeredItemDto.getStatus());
        this.registeredItemMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public RegisteredItem getOne(Long registeredItemId) {
        return this.registeredItemMapper.selectById(registeredItemId);
    }

    @Override
    public int addRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem=new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        registeredItem.setCreateTime(DateUtil.date());
        registeredItem.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        return this.registeredItemMapper.insert(registeredItem);
    }

    @Override
    public int updateRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem=new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        registeredItem.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        return this.registeredItemMapper.updateById(registeredItem);
    }

    @Override
    public int deleteRegisteredItemByIds(Long[] registeredItemIds) {
        List<Long> ids= Arrays.asList(registeredItemIds);
        if(ids.size()>0){
            return this.registeredItemMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<RegisteredItem> selectAllRegisteredItem() {
        QueryWrapper<RegisteredItem> qw=new QueryWrapper<>();
        qw.eq(RegisteredItem.COL_STATUS, Constants.STATUS_TRUE);
        return this.registeredItemMapper.selectList(qw);
    }
}
