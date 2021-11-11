package com.bjsxt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.DictData;
import com.bjsxt.dto.DictTypeDto;
import com.bjsxt.mapper.DictDataMapper;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.DictTypeMapper;
import com.bjsxt.domain.DictType;
import com.bjsxt.service.DictTypeService;
/**
* @Author: 尚学堂 雷哥
*/

@Service
public class DictTypeServiceImpl implements DictTypeService{

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {
        Page<DictType> page=new Page<>(dictTypeDto.getPageNum(),dictTypeDto.getPageSize());
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictName()),DictType.COL_DICT_NAME,dictTypeDto.getDictName());
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictType()),DictType.COL_DICT_TYPE,dictTypeDto.getDictType());
        qw.eq(StringUtils.isNotBlank(dictTypeDto.getStatus()),DictType.COL_STATUS,dictTypeDto.getStatus());

        qw.ge(dictTypeDto.getBeginTime()!=null,DictType.COL_CREATE_TIME,dictTypeDto.getBeginTime());
        qw.le(dictTypeDto.getEndTime()!=null,DictType.COL_CREATE_TIME,dictTypeDto.getEndTime());
        this.dictTypeMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public DataGridView list() {
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.dictTypeMapper.selectList(qw));
    }

    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId = (dictId == null) ? -1L : dictId;
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        DictType sysDictType = this.dictTypeMapper.selectOne(qw);
        if(null!=sysDictType &&dictId.longValue()!=sysDictType.getDictId().longValue()){
            return true; //说明不存在
        }
        return false; //说明存在
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        DictType dictType=new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        //设置创建者，。创建时间
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        dictType.setCreateTime(DateUtil.date());
        return this.dictTypeMapper.insert(dictType);
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        DictType dictType=new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        //设置修改人
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.dictTypeMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        List<Long> ids= Arrays.asList(dictIds);
        if(null!=ids&&ids.size()>0){
            return this.dictTypeMapper.deleteBatchIds(ids);
        }else{
            return -1;
        }
    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectById(dictId);
    }

    /**
     * 同步缓存的做法
     * 1，查询出所有可用的字典类型数据
     * 2，再根据字典的类型查询字典数据
     * 3，把字典数据生成json存到redis
     * 设计key
     * dict:dictType
     * 如dict:sys_user_sex --->[{},{},{}]
     *
     */
    @Override
    public void dictCacheAsync() {
        //查询出所有可用的字典类型数据
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS,Constants.STATUS_TRUE);
        List<DictType> dictTypes = this.dictTypeMapper.selectList(qw);
        for (DictType dictType : dictTypes) {
            QueryWrapper<DictData> qdw=new QueryWrapper<>();
            qdw.eq(DictData.COL_STATUS,Constants.STATUS_TRUE);
            qdw.eq(DictData.COL_DICT_TYPE,dictType.getDictType());
            qdw.orderByAsc(DictData.COL_DICT_SORT);
            List<DictData> dictDataList = dictDataMapper.selectList(qdw);
            //转成json串
            String json= JSON.toJSONString(dictDataList);
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(Constants.DICT_REDIS_PROFIX+dictType.getDictType(),json);
        }
    }
}
