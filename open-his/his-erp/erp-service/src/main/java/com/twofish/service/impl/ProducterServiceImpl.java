package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.Producter;
import com.twofish.dto.ProducterDto;
import com.twofish.mapper.ProducterMapper;
import com.twofish.service.ProducterService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@Service
public class ProducterServiceImpl  implements ProducterService{
    @Resource
    private ProducterMapper producterMapper;

    @Override
    public DataGridView listProducterForPage(ProducterDto producterDto) {
        Page<Producter> page = new Page<>(producterDto.getPageNum(),producterDto.getPageSize());
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(producterDto.getProducterName()),Producter.COL_PRODUCTER_NAME,producterDto.getProducterName());
        qw.like(StringUtils.isNotBlank(producterDto.getKeywords()),Producter.COL_KEYWORDS,producterDto.getKeywords());
        qw.like(StringUtils.isNotBlank(producterDto.getProducterTel()),Producter.COL_PRODUCTER_TEL,producterDto.getProducterTel());
        qw.eq(null!=producterDto.getStatus(),Producter.COL_STATUS,producterDto.getStatus());
        qw.ge(null!=producterDto.getBeginTime(),Producter.COL_CREATE_TIME,producterDto.getBeginTime());
        qw.le(null!=producterDto.getEndTime(),Producter.COL_CREATE_TIME,producterDto.getEndTime());
        producterMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addProducter(ProducterDto producterDto) {
        Producter producter = new Producter();
        BeanUtil.copyProperties(producterDto,producter);
        //设置创建人
        producter.setCreateBy(producterDto.getSimpleUser().getUserName());
        return producterMapper.insert(producter);
    }

    @Override
    public Producter getProducterById(Long producterId) {
        return this.producterMapper.selectById(producterId);
    }

    @Override
    public int updateProducter(ProducterDto producterDto) {
        Producter producter = new Producter();
        BeanUtil.copyProperties(producterDto,producter);
        //设置修改人
        producter.setUpdateBy(producterDto.getSimpleUser().getUserName());
        return producterMapper.updateById(producter);
    }

    @Override
    public int deleteProducterByIds(Long[] producterIds) {
        List<Long> ids = Arrays.asList(producterIds);
        if(ids!=null && ids.size()>0){
            return this.producterMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView selectAllProducter() {
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        qw.eq(Producter.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.producterMapper.selectList(qw));
    }
}
