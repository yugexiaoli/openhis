package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.Dept;
import com.twofish.dto.DeptDto;
import com.twofish.mapper.DeptMapper;
import com.twofish.service.DeptService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class DeptServiceImpl  implements DeptService{
    @Resource
    private DeptMapper deptMapper;

    @Override
    public DataGridView listDeptForPage(DeptDto deptDto) {
        Page<Dept> page = new Page<>(deptDto.getPageNum(),deptDto.getPageSize());
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(deptDto.getDeptName()),Dept.COL_DEPT_NAME,deptDto.getDeptName());
        qw.eq(null!=deptDto.getStatus(),Dept.COL_STATUS,deptDto.getStatus());
        qw.orderByDesc(Dept.COL_ORDER_NUM);
        deptMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        //设置创建人
        dept.setCreateBy(deptDto.getSimpleUser().getUserName());
        return deptMapper.insert(dept);
    }

    @Override
    public Dept getDeptById(Long deptId) {
        return this.deptMapper.selectById(deptId);
    }

    @Override
    public int updateDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        //设置修改人
        dept.setUpdateBy(deptDto.getSimpleUser().getUserName());
        return deptMapper.updateById(dept);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        List<Long> ids = Arrays.asList(deptIds);
        if(ids!=null && ids.size()>0){
            return this.deptMapper.deleteBatchIds(ids);
        }
       return -1;
    }

    @Override
    public DataGridView selectAllDept() {
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        qw.eq(Dept.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,this.deptMapper.selectList(qw));
    }

    //根据部门id集合查询部门表，查出部门集合
    @Override
    public List<Dept> queryDeptByIds(List<Long> deptIds) {
        QueryWrapper<Dept> qw = new QueryWrapper<>();
        qw.in(Dept.COL_DEPT_ID,deptIds);
        return this.deptMapper.selectList(qw);
    }

    @Override
    public Dept getOne(Long deptId) {
        QueryWrapper<Dept> qw=new QueryWrapper<>();
        qw.eq(Dept.COL_DEPT_ID,deptId);
        return this.deptMapper.selectOne(qw);
    }

    @Override
    public void updateDeptbyDeptId(Long deptId, Integer registrationNumber) {
        Dept dept = new Dept();
        dept.setDeptId(deptId);
        dept.setRegNumber(registrationNumber);
        this.deptMapper.updateById(dept);
    }
}
