package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.DictData;
import com.twofish.dto.DicTypeDto;
import com.twofish.mapper.DictDataMapper;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import com.twofish.mapper.DictTypeMapper;
import com.twofish.domain.DictType;
import com.twofish.service.DictTypeService;

@Service
public class DictTypeServiceImpl implements DictTypeService{
    @Resource
    private DictTypeMapper dictTypeMapper;
    @Resource
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


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

    /**
     * 缓存同步字典数据
     * 先查出所有可用的字典类型
     * 再根据字典类型查出所有可用的字典数据
     * 再转成json格式存到redis里
     * 格式：dict:类型 , [{},{},{}]
     */
    @Override
    public void dictCacheAsync() {
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS,Constants.STATUS_TRUE);
        List<DictType> dictTypes = this.dictTypeMapper.selectList(qw);
        for (DictType dictType : dictTypes) {
            QueryWrapper<DictData> qwd = new QueryWrapper<>();
            qwd.eq(DictData.COL_DICT_TYPE,dictType.getDictType());
            qwd.eq(DictData.COL_STATUS,Constants.STATUS_TRUE);
            qwd.orderByAsc(DictData.COL_DICT_SORT);
            List<DictData> dictData = this.dictDataMapper.selectList(qwd);
            redisTemplate.opsForValue().set(Constants.DICT_REDIS_PROFIX+dictType.getDictType(), JSON.toJSONString(dictData));
        }

    }
}
