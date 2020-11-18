package com.twofish.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.dto.generator.GenDto;
import com.twofish.mapper.SysGeneratorMapper;
import com.twofish.service.SysGeneratorService;
import com.twofish.vo.DataGridView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemGeneratorServiceImpl implements SysGeneratorService {
    @Resource
    private SysGeneratorMapper sysGeneratorMapper;

    @Override
    public DataGridView queryPage(GenDto dto) {
         Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
         this.sysGeneratorMapper.queryPage(page, dto.getTableName());
        return new DataGridView(page.getTotal(),page.getRecords());
    }





}
