package com.twofish.service;

import com.twofish.domain.Registration;
import com.twofish.dto.RegistrationDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface RegistrationService {

    //添加挂号
    void addRegistration(RegistrationDto registrationDto);

    //根据id查询挂号单
    Registration selectRgisionById(String regId);

    //现金收费，更改挂号状态为待就诊
    int updateRegisByRigId(Registration registration);

    //分页查询挂号列表
    DataGridView selectRegForPage(RegistrationDto registrationDto);

    //查询某状态的挂号信息
    List<Registration> queryRegstrationBySatatus(String scheudlingType, Long deptId, String registrationStatus, Long userId, String subsectionType);
}
