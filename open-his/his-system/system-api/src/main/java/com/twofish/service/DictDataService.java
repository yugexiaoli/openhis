package com.twofish.service;

import com.twofish.domain.DictData;
import com.twofish.dto.DicDataDto;
import com.twofish.dto.DicTypeDto;
import com.twofish.vo.DataGridView;

import java.util.List;

/**
 * 字典数据服务接口
 * @author ccy
 */
public interface DictDataService {
    /**
     * 分页查询字典数据
     * @param dicDataDto
     * @return
     */
    DataGridView listpage(DicDataDto dicDataDto);

    /**
     * 添加字典数据
     * @param dicDataDto
     * @return
     */
    int insertdictdata(DicDataDto dicDataDto);

    /**
     * 根据id查询字典数据
     * @param dictCode
     * @return
     */
    DictData querybyid(Long dictCode);

    /**
     * 修改字典数据
     * @param dicDataDto
     * @return
     */
    int updatedictdata(DicDataDto dicDataDto);

    /**
     * 根据id删除字典数据（可批量）
     * @param dictCode
     * @return
     */
    int deleteDictDataByIds(Long[] dictCode);

    /**
     * 根据字典类型查询字典
     * @param dictType
     * @return
     */
    List<DictData> querybydicttype(String dictType);

    /**
     * 从redis取字典缓存
     * @param dictType
     * @return
     */
    List<DictData> selectDicDataBydictType(String dictType);
}
