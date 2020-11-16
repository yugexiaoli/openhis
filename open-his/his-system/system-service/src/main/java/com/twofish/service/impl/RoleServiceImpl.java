package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.Role;
import com.twofish.dto.RoleDto;
import com.twofish.mapper.RoleMapper;
import com.twofish.vo.DataGridView;
import io.netty.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.twofish.service.RoleService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl  implements RoleService{
    @Resource
    private RoleMapper roleMapper;



    @Override
    public DataGridView listRoleForPage(RoleDto roleDto) {
        Page<Role> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(roleDto.getRoleName()),Role.COL_ROLE_NAME,roleDto.getRoleName());
        qw.like(StringUtils.isNotBlank(roleDto.getRoleCode()),Role.COL_ROLE_CODE,roleDto.getRoleCode());
        qw.eq(null!=roleDto.getStatus(),Role.COL_STATUS,roleDto.getStatus());
        qw.gt(null!=roleDto.getBeginTime(),Role.COL_CREATE_TIME,roleDto.getBeginTime());
        qw.lt(null!=roleDto.getEndTime(),Role.COL_CREATE_TIME,roleDto.getEndTime());
        qw.orderByAsc(Role.COL_ROLE_SORT);
        roleMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<Role> selectAllRole() {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq(Role.COL_STATUS, Constants.STATUS_TRUE);
        return this.roleMapper.selectList(qw);
    }

    @Override
    public int addRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto,role);
        //设置创建人
        role.setCreateBy(roleDto.getSimpleUser().getUserName());
        return this.roleMapper.insert(role);
    }

    @Override
    public Role getRoleById(Long roleId) {
        return this.roleMapper.selectById(roleId);
    }

    @Override
    public int updateRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto,role);
        //设置更新人
        role.setUpdateBy(roleDto.getSimpleUser().getUserName());
        return this.roleMapper.updateById(role);
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        List<Long> ids = Arrays.asList(roleIds);
        if(ids!=null && ids.size()>0){
            //删除role_menu和role_user表的数据
            this.roleMapper.deleteRoleMenuByRoleids(ids);
            this.roleMapper.deleteRoleUserByRoleids(ids);
            return this.roleMapper.deleteBatchIds(ids);
        }
        return -1;
    }


    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        //先删除原来角色拥有的菜单权限
        this.roleMapper.deleteRoleMenuByRoleids(Arrays.asList(roleId));
        //再插入权限
        for (Long menuId : menuIds) {
            this.roleMapper.insertRoleMenu(roleId,menuId);
        }
    }
}
