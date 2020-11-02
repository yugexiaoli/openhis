package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.DicTypeDto;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.mapper.DictTypeMapper;
import com.twofish.domain.DictType;
import com.twofish.service.DictTypeService;

@Service
public class DictTypeServiceImpl implements DictTypeService{
    @Resource
    private DictTypeMapper dictTypeMapper;


    @Override
    public DataGridView listpage(DicTypeDto dicTypeDto) {
        Page<DictType> page = new Page<>(dicTypeDto.getPageNum(),dicTypeDto.getPageSize());
        QueryWrapper<DictType> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dicTypeDto.getDictName()),DictType.COL_DICT_NAME,dicTypeDto.getDictName());
        wrapper.like(StringUtils.isNotBlank(dicTypeDto.getDictType()),DictType.COL_DICT_TYPE,dicTypeDto.getDictType());
        wrapper.eq(StringUtils.isNotBlank(dicTypeDto.getStatus()),DictType.COL_STATUS,dicTypeDto.getStatus());
        wrapper.ge(null!=dicTypeDto.getBeginTime(),DictType.COL_CREATE_TIME,dicTypeDto.getBeginTime());
        wrapper.le(null!=dicTypeDto.getEndTime(),DictType.COL_CREATE_TIME,dicTypeDto.getEndTime());
        this.dictTypeMapper.selectPage(page, wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public DataGridView list() {
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.dictTypeMapper.selectList(qw));
    }

    @Override
    public int insertdictype(DicTypeDto dto) {
        DictType type = new DictType();
        BeanUtil.copyProperties(dto,type);
        type.setCreateBy(dto.getSimpleUser().getUserName());
        return this.dictTypeMapper.insert(type);

    }

    @Override
    public DictType selectdictypebyid(Long dictId) {
        return this.dictTypeMapper.selectById(dictId);
    }

    @Override
    public int updatedictype(DicTypeDto dto) {

        DictType type = new DictType();
        BeanUtil.copyProperties(dto,type);
        type.setUpdateBy(dto.getSimpleUser().getUserName());
        return  this.dictTypeMapper.updateById(type);
    }

    @Override
    public int deletedictypebyids(Long[] dictIds) {
        List<Long> ids = Arrays.asList(dictIds);
        if(null!=ids && ids.size()>0){
           return this.dictTypeMapper.deleteBatchIds(ids);
        }else {
            return -1;
        }
    }

    @Override
    public Boolean checkdictypeuniq(Long dictId, String dictType) {
        dictId=(dictId==null) ? -1L : dictId;

        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE,dictType);
        DictType type = dictTypeMapper.selectOne(qw);
        if(type!=null&&type.getDictId().longValue()!=dictId.longValue()){
            //类型存在,不是唯一的
            return false;
        }
        return true;
    }
}
