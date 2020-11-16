package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户ID查询角色IDS
     * @param userId
     * @return
     */
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 向sys_role_user插入一条数据
     * @param userId
     * @param roleId
     */
    void insertUserRole(@Param("userId") Long userId,@Param("roleId") Long roleId);
}