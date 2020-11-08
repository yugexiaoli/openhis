package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.Menu;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据菜单id查询它下面子节点的个数
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(Long menuId);
}