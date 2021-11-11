package com.bjsxt.service;

import com.bjsxt.domain.DictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.DictDataDto;
import com.bjsxt.vo.DataGridView;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface DictDataService {
    /**
     * 分页查询字典数据类型
     *
     * @param dictDataDto
     * @return
     */
    DataGridView listPage(DictDataDto dictDataDto);


    /**
     * 插入新的字典类型
     *
     * @param dictDataDto
     * @return
     */
    int insert(DictDataDto dictDataDto);

    /**
     * 修改的字典类型
     *
     * @param dictDataDto
     * @return
     */
    int update(DictDataDto dictDataDto);

    /**
     * 根据ID删除字典类型
     *
     * @param dictCodeIds
     * @return
     */
    int deleteDictDataByIds(Long[] dictCodeIds);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType
     * @return
     */
    List<DictData> selectDictDataByDictType(String dictType);

    /**
     * 根据ID查询一个字典类型
     *
     * @param dictCode
     * @return
     */
    DictData selectDictDataById(Long dictCode);

}
