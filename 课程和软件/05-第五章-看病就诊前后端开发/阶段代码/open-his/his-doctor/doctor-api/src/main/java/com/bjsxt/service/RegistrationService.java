package com.bjsxt.service;

import com.bjsxt.domain.Registration;
import com.bjsxt.dto.RegistrationDto;
import com.bjsxt.vo.DataGridView;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */

public interface RegistrationService {

    /**
     * 添加挂号信息
     * @param registrationDto
     */
    void addRegistration(RegistrationDto registrationDto);

    /**
     * 根据挂号单ID查询挂号单信息
     * @param regId
     * @return
     */
    Registration queryRegistrationByRegId(String regId);

    /**
     * 根据ID更新挂号单的信息
     * @param registration
     * @return
     */
    int updateRegistrationByRegId(Registration registration);

    /**
     * 分页查询挂号单
     * @param registrationDto
     * @return
     */
    DataGridView queryRegistrationForPage(RegistrationDto registrationDto);

    /**
     * 根据条件查询挂号信息
     * @param deptId 部门
     * @param subsectionType  时段
     * @param scheudlingType  类型  门诊 急诊
     * @param regStatus    挂号单状态
     * @param userId   医生ID
     * @return
     */
    List<Registration> queryRegistration(Long deptId, String subsectionType, String scheudlingType, String regStatus, Long userId);
}
