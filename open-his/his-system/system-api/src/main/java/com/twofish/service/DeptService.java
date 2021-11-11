package com.twofish.service;

import com.twofish.domain.Dept;
import com.twofish.dto.DeptDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface DeptService {

    /**
     * 分页查询所有科室
     * @param deptDto
     * @return
     */
    DataGridView listDeptForPage(DeptDto deptDto);

    /**
     * 添加科室
     * @param deptDto
     * @return
     */
    int addDept(DeptDto deptDto);

    /**
     * 根据id查询科室
     * @param deptId
     * @return
     */
    Dept getDeptById(Long deptId);

    /**
     * 修改科室
     * @param deptDto
     * @return
     */
    int updateDept(DeptDto deptDto);

    /**
     * 删除科室(可批量)
     * @param deptIds
     * @return
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 查询所有可用的科室
     * @return
     */
    DataGridView selectAllDept();

    //再根据部门id集合查询部门表，查出部门集合
    List<Dept> queryDeptByIds(List<Long> deptIds);

    //根据一个科室id查询一个科室
    Dept getOne(Long deptId);

    //更新科室的号数
    void updateDeptbyDeptId(Long deptId, Integer registrationNumber);
}
