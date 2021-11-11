package com.bjsxt.service.impl;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.Medicines;
import com.bjsxt.domain.Medicines;
import com.bjsxt.dto.MedicinesDto;
import com.bjsxt.mapper.MedicinesMapper;
import com.bjsxt.mapper.MedicinesMapper;
import com.bjsxt.service.MedicinesService;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: 尚学堂 雷哥
 * methods = {@Method(name = "Medicines",retries = 0)
 * 代表当前类里面的Medicines这个主就去只调一次，不重试
 */
@Service(methods = {@Method(name = "addMedicines", retries = 0)})
public class MedicinesServiceImpl implements MedicinesService {


    @Autowired
    private MedicinesMapper medicinesMapper;

    @Override
    public DataGridView listMedicinesPage(MedicinesDto medicinesDto) {
        Page<Medicines> page=new Page<>(medicinesDto.getPageNum(),medicinesDto.getPageSize());
        QueryWrapper<Medicines> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(medicinesDto.getMedicinesName()),Medicines.COL_MEDICINES_NAME,medicinesDto.getMedicinesName());
        qw.like(StringUtils.isNotBlank(medicinesDto.getKeywords()),Medicines.COL_KEYWORDS,medicinesDto.getKeywords());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getMedicinesType()),Medicines.COL_MEDICINES_TYPE,medicinesDto.getMedicinesType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getProducterId()),Medicines.COL_PRODUCTER_ID,medicinesDto.getProducterId());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getPrescriptionType()),Medicines.COL_PRESCRIPTION_TYPE,medicinesDto.getPrescriptionType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getStatus()),Medicines.COL_STATUS,medicinesDto.getStatus());
        this.medicinesMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Medicines getOne(Long medicinesId) {
        return this.medicinesMapper.selectById(medicinesId);
    }

    @Override
    public int addMedicines(MedicinesDto medicinesDto) {
        Medicines medicines=new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        medicines.setCreateTime(DateUtil.date());
        medicines.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        return this.medicinesMapper.insert(medicines);
    }

    @Override
    public int updateMedicines(MedicinesDto medicinesDto) {
        Medicines medicines=new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        medicines.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return this.medicinesMapper.updateById(medicines);
    }

    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> ids= Arrays.asList(medicinesIds);
        if(ids.size()>0){
            return this.medicinesMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Medicines> selectAllMedicines() {
        QueryWrapper<Medicines> qw=new QueryWrapper<>();
        qw.eq(Medicines.COL_STATUS, Constants.STATUS_TRUE);
        return this.medicinesMapper.selectList(qw);
    }

    @Override
    public int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum) {
        Medicines medicines=new Medicines();
        medicines.setMedicinesId(medicinesId);
        medicines.setMedicinesStockNum(medicinesStockNum);
        return this.medicinesMapper.updateById(medicines);
    }
}
