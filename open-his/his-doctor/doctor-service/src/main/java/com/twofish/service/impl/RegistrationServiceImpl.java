package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.domain.Registration;
import com.twofish.dto.RegistrationDto;
import com.twofish.mapper.RegistrationMapper;
import com.twofish.service.RegistrationService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    @Resource
    private RegistrationMapper registrationMapper;

    @Override
    public void addRegistration(RegistrationDto registrationDto) {
        Registration registration = new Registration();
        BeanUtil.copyProperties(registrationDto,registration);
//        registration.setUserId((Long) registrationDto.getSimpleUser().getUserId());
//        registration.setDoctorName(registrationDto.getSimpleUser().getUserName());
//        registration.setCreateTime(DateUtil.date());
        registration.setCreateBy(registrationDto.getSimpleUser().getUserName());

        this.registrationMapper.insert(registration);
    }

    @Override
    public Registration selectRgisionById(String regId) {
        return this.registrationMapper.selectById(regId);
    }

    @Override
    public int updateRegisByRigId(Registration registration) {
        return this.registrationMapper.updateById(registration);
    }

    @Override
    public DataGridView selectRegForPage(RegistrationDto registrationDto) {
        Page<Registration> page = new Page<>(registrationDto.getPageNum(),registrationDto.getPageSize());
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(registrationDto.getDeptId()!=null,Registration.COL_DEPT_ID,registrationDto.getDeptId());
        qw.likeRight(StringUtils.isNotBlank(registrationDto.getPatientName()),Registration.COL_PATIENT_NAME,registrationDto.getPatientName());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSubsectionType()),Registration.COL_SUBSECTION_TYPE,registrationDto.getSubsectionType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSchedulingType()),Registration.COL_SCHEDULING_TYPE,registrationDto.getSchedulingType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getRegistrationStatus()),Registration.COL_REGISTRATION_STATUS,registrationDto.getRegistrationStatus());
        qw.ge(registrationDto.getBeginTime()!=null,Registration.COL_VISIT_DATE,registrationDto.getBeginTime());
        qw.le(registrationDto.getEndTime()!=null,Registration.COL_VISIT_DATE,registrationDto.getEndTime());
//        System.out.println("============="+registrationDto.getVisitDate());
        qw.eq(StringUtils.isNotBlank(registrationDto.getVisitDate()),Registration.COL_VISIT_DATE,registrationDto.getVisitDate());
        qw.orderByDesc(Registration.COL_CREATE_TIME);
        this.registrationMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<Registration> queryRegstrationBySatatus(String scheudlingType, Long deptId, String registrationStatus, Long userId, String subsectionType) {
        QueryWrapper<Registration> qw = new QueryWrapper<>();
        qw.eq(null!=userId,Registration.COL_USER_ID,userId);
        qw.eq(Registration.COL_SCHEDULING_TYPE,scheudlingType);
        qw.eq(StringUtils.isNotBlank(subsectionType),Registration.COL_SUBSECTION_TYPE,subsectionType);
        qw.eq(Registration.COL_REGISTRATION_STATUS,registrationStatus);
        qw.eq(Registration.COL_DEPT_ID,deptId);
        //只查询当天的
        qw.eq(Registration.COL_VISIT_DATE, DateUtil.format((DateUtil.date()),"yyyy-MM-dd"));
        return this.registrationMapper.selectList(qw);
    }
}
