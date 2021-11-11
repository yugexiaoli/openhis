package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.domain.Patient;
import com.twofish.domain.PatientFile;
import com.twofish.dto.PatientDto;
import com.twofish.mapper.PatientFileMapper;
import com.twofish.mapper.PatientMapper;
import com.twofish.service.PatientService;
import com.twofish.utils.AppMd5Utils;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

@Service
public class PatientServiceImpl implements PatientService{

    @Resource
    private PatientMapper patientMapper;
    @Resource
    private PatientFileMapper patientFileMapper;


    @Override
    public DataGridView listPatientForPage(PatientDto patientDto) {
        Page<Patient> page = new Page<>(patientDto.getPageNum(), patientDto.getPageSize());
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(patientDto.getName()),Patient.COL_NAME,patientDto.getName());
        qw.like(StringUtils.isNotBlank(patientDto.getPhone()),Patient.COL_PHONE,patientDto.getPhone());
        qw.like(StringUtils.isNotBlank(patientDto.getIdCard()),Patient.COL_ID_CARD,patientDto.getIdCard());
        this.patientMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Patient getPatientById(String patientId) {
        return this.patientMapper.selectById(patientId);
    }

    @Override
    public PatientFile getPatientFileById(String patientId) {
        return this.patientFileMapper.selectById(patientId);
    }

    @Override
    public Patient getPatientByIdCard(String idCard) {
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        qw.eq(Patient.COL_ID_CARD,idCard);
        return this.patientMapper.selectOne(qw);
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient=new Patient();
        BeanUtil.copyProperties(patientDto,patient);
        patient.setPassword(AppMd5Utils.md5(patient.getPhone().substring(5),patient.getPhone(),2));
//        patient.setCreateTime(DateUtil.date());
        this.patientMapper.insert(patient);
        return patient;
    }
}
