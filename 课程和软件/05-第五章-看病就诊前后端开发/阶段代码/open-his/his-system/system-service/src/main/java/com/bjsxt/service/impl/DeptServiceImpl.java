package com.bjsxt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.Dept;
import com.bjsxt.dto.DeptDto;
import com.bjsxt.mapper.DeptMapper;
import com.bjsxt.service.DeptService;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
* @Author: 尚学堂 雷哥
*/

@Service
public class DeptServiceImpl  implements DeptService{

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public DataGridView listPage(DeptDto deptDto) {
        Page<Dept> page=new Page<>(deptDto.getPageNum(),deptDto.getPageSize());
        QueryWrapper<Dept> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(deptDto.getDeptName()),Dept.COL_DEPT_NAME,deptDto.getDeptName());
        qw.eq(StringUtils.isNotBlank(deptDto.getStatus()),Dept.COL_STATUS,deptDto.getStatus());

        qw.ge(deptDto.getBeginTime()!=null,Dept.COL_CREATE_TIME,deptDto.getBeginTime());
        qw.le(deptDto.getEndTime()!=null,Dept.COL_CREATE_TIME,deptDto.getEndTime());

        qw.orderByAsc(Dept.COL_ORDER_NUM);
        this.deptMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<Dept> list() {
        QueryWrapper<Dept> qw=new QueryWrapper<>();
        qw.eq(Dept.COL_STATUS, Constants.STATUS_TRUE);
        qw.orderByAsc(Dept.COL_ORDER_NUM);
        return this.deptMapper.selectList(qw);
    }

    @Override
    public Dept getOne(Long deptId) {
        return this.deptMapper.selectById(deptId);
    }

    @Override
    public int addDept(DeptDto deptDto) {
        Dept dept=new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        //设置创建人，创建时间
        dept.setCreateTime(DateUtil.date());
        dept.setCreateBy(deptDto.getSimpleUser().getUserName());
        return this.deptMapper.insert(dept);
    }

    @Override
    public int updateDept(DeptDto deptDto) {
        Dept dept=new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        //设置修改人
        dept.setUpdateBy(deptDto.getSimpleUser().getUserName());
        return this.deptMapper.updateById(dept);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        List<Long> ids = Arrays.asList(deptIds);
        if(ids!=null&&ids.size()>0){
            return this.deptMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Dept> listDeptByDeptIds(List<Long> deptIds) {
        if(deptIds!=null&&deptIds.size()>0){
            QueryWrapper<Dept> qw=new QueryWrapper<>();
            qw.in(Dept.COL_DEPT_ID,deptIds);
            return this.deptMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void updateDeptRegNumber(Long deptId, int i) {
        Dept dept=new Dept();
        dept.setDeptId(deptId);
        dept.setRegNumber(i);
        this.deptMapper.updateById(dept);
    }
}
