package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.User;
import com.twofish.dto.UserDto;
import com.twofish.mapper.RoleMapper;
import com.twofish.mapper.UserMapper;
import com.twofish.service.UserService;
import com.twofish.utils.AppMd5Utils;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public User querybyphone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(User.COL_PHONE,phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getone(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        Page<User> page = new Page<>(userDto.getPageNum(),userDto.getPageSize());
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq(null!=userDto.getDeptId(),User.COL_DEPT_ID,userDto.getDeptId());
        qw.like(StringUtils.isNotBlank(userDto.getUserName()),User.COL_USER_NAME,userDto.getUserName());
        qw.likeRight(StringUtils.isNotBlank(userDto.getPhone()),User.COL_PHONE,userDto.getPhone());
        qw.eq(null!=userDto.getStatus(),User.COL_STATUS,userDto.getStatus());
        qw.ge(null!=userDto.getBeginTime(),User.COL_CREATE_TIME,userDto.getBeginTime());
        qw.le(null!=userDto.getEndTime(),User.COL_CREATE_TIME,userDto.getEndTime());
        qw.orderByDesc(User.COL_CREATE_TIME);
        this.userMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    
    @Override
    public int addUser(UserDto userDto) {
        User user = new User();
        BeanUtil.copyProperties(userDto,user);
        user.setUserType(Constants.USER_NORMAL);
        user.setSalt(AppMd5Utils.createSalt());
        String sourcepass=userDto.getPhone().substring(5);//明文密码
        user.setPassword(AppMd5Utils.md5(sourcepass,user.getSalt(),2));//设置密文密码
        user.setCreateBy(userDto.getSimpleUser().getUserName());//创建人
        return this.userMapper.insert(user);
    }

    @Override
    public int updateUser(UserDto userDto) {
        Long userId = userDto.getUserId();
        User user = this.userMapper.selectById(userId);
        if(null==user){
            return 0;
        }
        BeanUtil.copyProperties(userDto,user);
        user.setUpdateBy(userDto.getSimpleUser().getUserName());//修改人
        return this.userMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        List<Long> ids = Arrays.asList(userIds);
        if(null!=ids && ids.size()>0){
            //先根据用户ids删除sys_role_user数据
            this.roleMapper.deleteRoleUserByUserids(ids);
            return this.userMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public void resetPwd(Long[] userIds) {
        for (Long userId : userIds) {
            User user = this.userMapper.selectById(userId);
            String sourcePass=""; //明文密码
           if(Constants.USER_ADMIN==user.getUserType()){
               //超级用户 设置明文密码为123456
               sourcePass="123456";
           }else {
               //系统普通用户，设置明文密码为手机号后六位
               sourcePass=user.getPhone().substring(5);
           }
           user.setSalt(AppMd5Utils.createSalt());
           user.setPassword(AppMd5Utils.md5(sourcePass,user.getSalt(),2));
           this.userMapper.updateById(user);
        }
    }

    @Override
    public List<User> selectAllUser() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq(User.COL_STATUS,Constants.STATUS_TRUE);
        qw.eq(User.COL_USER_TYPE,Constants.USER_NORMAL);
        return this.userMapper.selectList(qw);
    }

    @Override
    public Boolean checkphonehasexit(String phone,Long userId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq(User.COL_PHONE,phone);
        User user = this.userMapper.selectOne(qw);
        if(user!=null && user.getUserId().longValue()!=userId.longValue()){
            //手机号已存在
            return true;
        }
        return false;
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return this.userMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public void saveUserRole(Long userId, Long[] roleIds) {
        //先根据用户id删除之前的角色，再插入新的角色
        this.roleMapper.deleteRoleUserByUserids(Arrays.asList(userId));
        for (Long roleId : roleIds) {
            this.userMapper.insertUserRole(userId,roleId);
        }
    }

    @Override
    public List<User> getUsersNeedScheduling() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq(User.COL_SCHEDULING_FLAG,Constants.SCHEDULING_FLAG_TRUE);
        return this.userMapper.selectList(qw);
    }

    @Override
    public List<User> queryUsersNeedScheduling(Long userId, Long deptId) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(null!=userId,User.COL_USER_ID,userId);
        qw.eq(null!=deptId,User.COL_DEPT_ID,deptId);
        qw.eq(User.COL_STATUS,Constants.STATUS_TRUE);
        qw.eq(User.COL_SCHEDULING_FLAG,Constants.SCHEDULING_FLAG_TRUE);
        return this.userMapper.selectList(qw);
    }
}
