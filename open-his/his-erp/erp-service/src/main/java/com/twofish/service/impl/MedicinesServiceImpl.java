package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.Medicines;
import com.twofish.dto.MedicinesDto;
import com.twofish.mapper.MedicinesMapper;
import com.twofish.service.MedicinesService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class MedicinesServiceImpl  implements MedicinesService{
    @Resource
    private MedicinesMapper medicinesMapper;

    @Override
    public DataGridView listMedicinesForPage(MedicinesDto medicinesDto) {
        Page<Medicines> page = new Page<>(medicinesDto.getPageNum(),medicinesDto.getPageSize());
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(medicinesDto.getMedicinesName()),Medicines.COL_MEDICINES_NAME,medicinesDto.getMedicinesName());
        qw.like(StringUtils.isNotBlank(medicinesDto.getKeywords()),Medicines.COL_KEYWORDS,medicinesDto.getKeywords());
        qw.eq(null!=medicinesDto.getMedicinesType(),Medicines.COL_MEDICINES_TYPE,medicinesDto.getMedicinesType());
        qw.eq(null!=medicinesDto.getProducterId(),Medicines.COL_PRODUCTER_ID,medicinesDto.getProducterId());
        qw.eq(null!=medicinesDto.getPrescriptionType(),Medicines.COL_PRESCRIPTION_TYPE,medicinesDto.getPrescriptionType());
        qw.eq(null!=medicinesDto.getStatus(),Medicines.COL_STATUS,medicinesDto.getStatus());
        medicinesMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        //设置创建人
        medicines.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        return medicinesMapper.insert(medicines);
    }

    @Override
    public Medicines getMedicinesById(Long medicinesId) {
        return this.medicinesMapper.selectById(medicinesId);
    }

    @Override
    public int updateMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        //设置修改人
        medicines.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return medicinesMapper.updateById(medicines);
    }

    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> ids = Arrays.asList(medicinesIds);
        if(ids!=null && ids.size()>0){
            return this.medicinesMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView selectAllMedicines() {
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        qw.eq(Medicines.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.medicinesMapper.selectList(qw));
    }

    @Override
    public int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum) {
        if(medicinesId !=null && medicinesStockNum !=null){
            return this.medicinesMapper.updateMedicinesByIdAndNum(medicinesId,medicinesStockNum);
        }
        return -1;
    }
}
