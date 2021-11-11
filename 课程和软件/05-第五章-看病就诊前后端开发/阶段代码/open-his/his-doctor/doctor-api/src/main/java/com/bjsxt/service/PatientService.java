package com.bjsxt.service;

import com.bjsxt.domain.Patient;
import com.bjsxt.domain.PatientFile;
import com.bjsxt.dto.PatientDto;
import com.bjsxt.vo.DataGridView;

/**
* @Author: 尚学堂 雷哥
*/

public interface PatientService {

    /**
     * 分页查询
     * @param patientDto
     * @return
     */
    DataGridView listPatientForPage(PatientDto patientDto);

    /**
     * 根据患者ID查询患者信息
     * @param patientId
     * @return
     */
    Patient getPatientById(String patientId);

    /**
     * 根据患者ID查询患者档案信息
     * @param patientId
     * @return
     */
    PatientFile getPatientFileById(String patientId);


    /**
     * 根据身份证号查询患者信息
     * @param idCard
     * @return
     */
    Patient getPatientByIdCard(String idCard);

    /**
     * 添加患者
     * @param patientDto
     * @return
     */
    Patient addPatient(PatientDto patientDto);
}
