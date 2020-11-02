package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.dto.DicDataDto;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twofish.domain.DictData;
import com.twofish.mapper.DictDataMapper;
import com.twofish.service.DictDataService;
@Service
public class DictDataServiceImpl implements DictDataService{
    @Resource
    private DictDataMapper dataMapper;

    @Override
    public DataGridView listpage(DicDataDto dicDataDto) {
        Page<DictData> page = new Page<>(dicDataDto.getPageNum(),dicDataDto.getPageSize());
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(dicDataDto.getDictType()),DictData.COL_DICT_TYPE,dicDataDto.getDictType());
        qw.like(StringUtils.isNotBlank(dicDataDto.getDictLabel()),DictData.COL_DICT_LABEL,dicDataDto.getDictLabel());
        qw.eq(StringUtils.isNotBlank(dicDataDto.getStatus()),DictData.COL_STATUS,dicDataDto.getStatus());
        dataMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int insertdictdata(DicDataDto dicDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dicDataDto,dictData);
        //设置创建人
        dictData.setCreateBy(dicDataDto.getSimpleUser().getUserName());
        return dataMapper.insert(dictData);
    }

    @Override
    public DictData querybyid(Long dictCode) {
        return dataMapper.selectById(dictCode);
    }

    @Override
    public int updatedictdata(DicDataDto dicDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dicDataDto,dictData);
        //设置创建人
        dictData.setUpdateBy(dicDataDto.getSimpleUser().getUserName());
        return dataMapper.updateById(dictData);
    }

    @Override
    public int deleteDictDataByIds(Long[] dictCode) {
        List<Long> ids = Arrays.asList(dictCode);
        if(ids!=null&&ids.size()>0){
            return dataMapper.deleteBatchIds(ids);
        }
       return -1;
    }

    @Override
    public List<DictData> querybydicttype(String dictType) {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        qw.eq(DictData.COL_DICT_TYPE,dictType);
        //显示可用字典数据
        qw.eq(DictData.COL_STATUS, Constants.STATUS_TRUE);
        qw.orderByAsc(DictData.COL_DICT_SORT);
        return dataMapper.selectList(qw);
    }
}
