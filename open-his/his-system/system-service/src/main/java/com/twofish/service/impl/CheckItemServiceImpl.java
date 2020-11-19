package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.CheckItem;
import com.twofish.dto.CheckItemDto;
import com.twofish.mapper.CheckItemMapper;
import com.twofish.service.CheckItemService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class CheckItemServiceImpl  implements CheckItemService{
    @Resource
    private CheckItemMapper checkItemMapper;

    @Override
    public DataGridView listCheckItemForPage(CheckItemDto checkItemDto) {
        Page<CheckItem> page = new Page<>(checkItemDto.getPageNum(),checkItemDto.getPageSize());
        QueryWrapper<CheckItem> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(checkItemDto.getCheckItemName()),CheckItem.COL_CHECK_ITEM_NAME,checkItemDto.getCheckItemName());
        qw.like(StringUtils.isNotBlank(checkItemDto.getKeywords()),CheckItem.COL_KEYWORDS,checkItemDto.getKeywords());
        qw.eq(null!=checkItemDto.getTypeId(),CheckItem.COL_TYPE_ID,checkItemDto.getTypeId());
        qw.eq(null!=checkItemDto.getStatus(),CheckItem.COL_STATUS,checkItemDto.getStatus());
        checkItemMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        BeanUtil.copyProperties(checkItemDto,checkItem);
        //设置创建人
        checkItem.setCreateBy(checkItemDto.getSimpleUser().getUserName());
        return checkItemMapper.insert(checkItem);
    }

    @Override
    public CheckItem getCheckItemById(Long checkItemId) {
        return this.checkItemMapper.selectById(checkItemId);
    }

    @Override
    public int updateCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        BeanUtil.copyProperties(checkItemDto,checkItem);
        //设置修改人
        checkItem.setUpdateBy(checkItemDto.getSimpleUser().getUserName());
        return checkItemMapper.updateById(checkItem);
    }

    @Override
    public int deleteCheckItemByIds(Long[] checkItemIds) {
        List<Long> ids = Arrays.asList(checkItemIds);
        if(ids!=null && ids.size()>0){
            return this.checkItemMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView selectAllCheckItem() {
        QueryWrapper<CheckItem> qw = new QueryWrapper<>();
        qw.eq(CheckItem.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.checkItemMapper.selectList(qw));
    }
}
