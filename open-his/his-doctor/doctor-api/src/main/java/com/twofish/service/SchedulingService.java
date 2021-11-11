package com.twofish.service;

import com.twofish.domain.Scheduling;
import com.twofish.dto.SchedulingFormDto;
import com.twofish.dto.SchedulingQueryDto;

import java.util.List;

public interface SchedulingService {


    //根据科室用户和排班周查询
    List<Scheduling> queryForScheduling(SchedulingQueryDto schedulingQueryDto);

    int saveScheduling(SchedulingFormDto formDto);

    //先根据条件查询排班表，查出部门id集合
    List<Long> queryHasSchedulingForDeptId(Long deptId, String schedulingDay, String schedulingType, String subsectionType);
}
