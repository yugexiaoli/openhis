package com.twofish.service;

import com.twofish.domain.Patient;
import com.twofish.domain.PatientFile;
import com.twofish.dto.PatientDto;
import com.twofish.vo.DataGridView;

public interface PatientService {

    //分页查询所有患者信息
    DataGridView listPatientForPage(PatientDto patientDto);

    //根据患者ID查询患者信息
    Patient getPatientById(String patientId);

    //根据患者ID查询患者的档案信息
    PatientFile getPatientFileById(String patientId);

    //根据身份证查询患者信息
    Patient getPatientByIdCard(String idCard);

    //添加患者
    Patient addPatient(PatientDto patientDto);
}
