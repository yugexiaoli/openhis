package com.twofish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twofish.domain.Scheduling;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SchedulingMapper extends BaseMapper<Scheduling> {
    List<Long> queryHasSchedulingForDeptId(@Param("deptId") Long deptId, @Param("schedulingDay") String schedulingDay, @Param("schedulingType") String schedulingType, @Param("subsectionType") String subsectionType);
}