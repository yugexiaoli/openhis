package com.twofish.service;

import com.twofish.dto.generator.GenDto;
import com.twofish.vo.DataGridView;

public interface SysGeneratorService {

    /**
     * 分页查询所有数据库表
     * @param dto
     * @return
     */
    DataGridView queryPage(GenDto dto);

}
