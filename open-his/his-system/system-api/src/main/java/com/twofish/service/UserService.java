package com.twofish.service;

import com.twofish.domain.User;
import com.twofish.dto.UserDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface UserService {

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    User querybyphone(String phone);

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    User getone(Long userId);

    /**
     * 分页查询用户信息
     * @param userDto
     * @return
     */
    DataGridView listUserForPage(UserDto userDto);

    /**
     * 添加用户信息
     * @param userDto
     * @return
     */
    int addUser(UserDto userDto);



    /**
     * 修改用户信息
     * @param userDto
     * @return
     */
    int updateUser(UserDto userDto);


    /**
     * 根据ID删除用户信息
     * @param userIds
     * @return
     */
    int deleteUserByIds(Long[] userIds);


    /**
     * 重置用户密码
     * @param userIds
     */
    void resetPwd(Long[] userIds);


    /**
     * 查询所有可用的系统用户信息
     * @return
     */
    List<User> selectAllUser();

    /**
     * 检查手机号是否重复【修改和添加用户时判断】
     * @param phone
     * @param userId
     * @return
     */
    Boolean checkphonehasexit(String phone,Long userId);

    /**
     * 根据用户ID查询角色IDS
     * @param userId
     * @return
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 保存用户和角色之间的关系
     * @param userId
     * @param roleIds
     */
    void saveUserRole(Long userId, Long[] roleIds);

    /**
     * 查询需要排班的医生信息(弃用)
     * @return
     */
    List<User> getUsersNeedScheduling();

    /**
     * 查询要排版的医生信息(根据用户id和部门id,状态可用)
     * @param userId
     * @param deptId
     * @return
     */
    List<User> queryUsersNeedScheduling(Long userId, Long deptId);
}
