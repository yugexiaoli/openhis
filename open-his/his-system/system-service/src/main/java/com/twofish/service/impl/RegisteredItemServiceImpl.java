package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.RegisteredItem;
import com.twofish.dto.RegisteredItemDto;
import com.twofish.mapper.RegisteredItemMapper;
import com.twofish.service.RegisteredItemService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class RegisteredItemServiceImpl  implements RegisteredItemService{
    @Resource
    private RegisteredItemMapper registeredItemMapper;

    @Override
    public DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto) {
        Page<RegisteredItem> page = new Page<>(registeredItemDto.getPageNum(),registeredItemDto.getPageSize());
        QueryWrapper<RegisteredItem> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(registeredItemDto.getRegItemName()),RegisteredItem.COL_REG_ITEM_NAME,registeredItemDto.getRegItemName());
        qw.eq(null!=registeredItemDto.getStatus(),RegisteredItem.COL_STATUS,registeredItemDto.getStatus());
        registeredItemMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        //设置创建人
        registeredItem.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        return registeredItemMapper.insert(registeredItem);
    }

    @Override
    public RegisteredItem getRegisteredItemById(Long registeredItemId) {
        return this.registeredItemMapper.selectById(registeredItemId);
    }

    @Override
    public int updateRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        //设置修改人
        registeredItem.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        return registeredItemMapper.updateById(registeredItem);
    }

    @Override
    public int deleteRegisteredItemByIds(Long[] registeredItemIds) {
        List<Long> ids = Arrays.asList(registeredItemIds);
        if(ids!=null && ids.size()>0){
            return this.registeredItemMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView selectAllRegisteredItem() {
        QueryWrapper<RegisteredItem> qw = new QueryWrapper<>();
        qw.eq(RegisteredItem.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.registeredItemMapper.selectList(qw));
    }
}
