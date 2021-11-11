package com.bjsxt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.Registration;
import com.bjsxt.dto.RegistrationDto;
import com.bjsxt.mapper.RegistrationMapper;
import com.bjsxt.service.RegistrationService;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* @Author: 尚学堂 雷哥
*/

@Service
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    private RegistrationMapper registrationMapper;

    @Override
    public void addRegistration(RegistrationDto registrationDto) {
        Registration registration=new Registration();
        BeanUtil.copyProperties(registrationDto,registration);
        registration.setRegStatus(Constants.REG_STATUS_0);//挂号添加为未收费
        registration.setCreateBy(registrationDto.getSimpleUser().getUserName());
        registration.setCreateTime(DateUtil.date());
        this.registrationMapper.insert(registration);
    }

    @Override
    public Registration queryRegistrationByRegId(String regId) {
        return this.registrationMapper.selectById(regId);
    }

    @Override
    public int updateRegistrationByRegId(Registration registration) {
        return this.registrationMapper.updateById(registration);
    }

    @Override
    public DataGridView queryRegistrationForPage(RegistrationDto registrationDto) {
        Page<Registration> page=new Page<>(registrationDto.getPageNum(),registrationDto.getPageSize());
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(registrationDto.getDeptId()!=null,Registration.COL_DEPT_ID,registrationDto.getDeptId());
        qw.like(StringUtils.isNotBlank(registrationDto.getPatientName()),Registration.COL_PATIENT_NAME,registrationDto.getPatientName());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSchedulingType()),Registration.COL_SCHEDULING_TYPE,registrationDto.getSchedulingType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSubsectionType()),Registration.COL_SUBSECTION_TYPE,registrationDto.getSubsectionType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getRegStatus()),Registration.COL_REG_STATUS,registrationDto.getRegStatus());
        qw.eq(StringUtils.isNotBlank(registrationDto.getVisitDate()),Registration.COL_VISIT_DATE,registrationDto.getVisitDate());
        qw.orderByDesc(Registration.COL_CREATE_TIME);
        this.registrationMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据条件查询挂号信息
     * @param deptId 部门
     * @param subsectionType  时段
     * @param scheudlingType  类型  门诊 急诊
     * @param regStatus    挂号单状态
     * @param userId   医生ID
     * @return
     */
    @Override
    public List<Registration> queryRegistration(Long deptId, String subsectionType, String scheudlingType, String regStatus, Long userId) {
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(Registration.COL_DEPT_ID,deptId);
        qw.eq(StringUtils.isNotBlank(subsectionType),Registration.COL_SUBSECTION_TYPE,subsectionType);
        qw.eq(Registration.COL_SCHEDULING_TYPE,scheudlingType);
        qw.eq(Registration.COL_REG_STATUS,regStatus);
        qw.eq(Registration.COL_VISIT_DATE,DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        qw.eq(null!=userId,Registration.COL_USER_ID,userId);
        qw.orderByAsc(Registration.COL_REG_NUMBER);
        return this.registrationMapper.selectList(qw);
    }
}
