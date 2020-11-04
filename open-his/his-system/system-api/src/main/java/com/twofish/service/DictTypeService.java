package com.twofish.service;


import com.twofish.domain.DictType;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.DicTypeDto;
import com.twofish.vo.DataGridView;

/**
 * @author ccy
 */
public interface DictTypeService {

    /**
     * 分页多条件查询字典
     * @param dicTypeDto
     * @return
     */
    DataGridView listpage(DicTypeDto dicTypeDto);

    /**
     * 查询所有字典类型
     * @return
     */
    DataGridView list();

    /**
     * 新增字典
     * @param dicTypeDto
     * @return
     */
    int insertdictype(DicTypeDto dicTypeDto);

    /**
     * 查询单个字典
     * @param dictId
     * @return
     */
    DictType selectdictypebyid(Long dictId);

    /**
     * 修改字典
     * @param dicTypeDto
     * @return
     */
    int updatedictype(DicTypeDto dicTypeDto);

    /**
     * 删除字典(可以批量删除)
     * @param dictIds
     * @return
     */
    int deletedictypebyids(Long[] dictIds);

    /**
     * 检查字典类型是否存在
     * @param dictId
     * @param dictType
     * @return
     */
    Boolean checkdictypeuniq(Long dictId,String dictType);


    /**
     * 字典缓存同步
     */
    void dictCacheAsync();
}
