package com.twofish.service;

import com.twofish.domain.CareHistory;
import com.twofish.dto.CareHistoryDto;

import java.util.List;

public interface CareHistoryService {

    //根据患者id查询病例集合
    List<CareHistory> queryCareHistoryByPatientId(String patientId);

    //修改病例
    CareHistory updateHistory(CareHistoryDto careHistoryDto);

    //新增病例
    CareHistory addHistory(CareHistoryDto careHistoryDto);

    //根据挂号单id查询病例
    CareHistory getCareHistoryByRegId(String regId);
}
