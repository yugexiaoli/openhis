package com.bjsxt.service;

import com.bjsxt.domain.User;
import com.bjsxt.dto.UserDto;
import com.bjsxt.vo.DataGridView;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface UserService {

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户编号
     * @return
     */
    User getOne(Long userId);

    /**
     * 分页查询用户
     * @param userDto
     * @return
     */
    DataGridView listUserForPage(UserDto userDto);

    /**
     * 添加用户
     * @param userDto
     * @return
     */
    int addUser(UserDto userDto);

    /**
     * 修改用户
     * @param userDto
     * @return
     */
    int updateUser(UserDto userDto);

    /**
     * 删除用户
     * @param userIds
     * @return
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 重置用户密码
     * @param userIds
     */
    void resetPassWord(Long[] userIds);


    /**
     * 查询所有可用的用户
     * @return
     */
    List<User> getAllUsers();


    /**
     * 查询要排班的医生信息
     * 如果部门ID为空，那么查询所有要排班的用户信息
     */
    List<User> queryUsersNeedScheduling(Long userId,Long deptId);
}
