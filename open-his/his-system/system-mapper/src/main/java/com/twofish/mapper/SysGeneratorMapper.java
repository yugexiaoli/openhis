package com.twofish.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysGeneratorMapper {

    IPage<List<Map<String, Object>>> queryPage(Page page, @Param("tableName") String tableName);



}