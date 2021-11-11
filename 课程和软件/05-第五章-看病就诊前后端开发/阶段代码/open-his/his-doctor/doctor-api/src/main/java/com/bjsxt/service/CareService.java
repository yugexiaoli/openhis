package com.bjsxt.service;

import com.bjsxt.domain.CareHistory;
import com.bjsxt.domain.CareOrder;
import com.bjsxt.domain.CareOrderItem;
import com.bjsxt.dto.CareHistoryDto;
import com.bjsxt.dto.CareOrderFormDto;

import java.util.List;

/**
 * @Author: 尚学堂 雷哥
 */
public interface CareService {
    /**
     * 根据患者ID查询病历列表
     * @param patientId
     * @return
     */
    List<CareHistory> queryCareHistoryByPatientId(String patientId);

    /**
     * 保存或更新病例信息
     * @param careHistoryDto
     * @return
     */
    CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto);

    /**
     * 根据挂号单位ID查询对应的病历信息
     * @param regId
     * @return
     */
    CareHistory queryCareHistoryByRegId(String regId);

    /**
     * 根据病历信息查询处方信息
     * @param chId
     * @return
     */
    List<CareOrder> queryCareOrdersByChId(String chId);

    /**
     * 根据处方ID查询处方详情信息
     * @param coId
     * @return
     */
    List<CareOrderItem> queryCareOrderItemsByCoId(String coId);

    /**
     * 根据病例ID查询病历信息
     * @param chId
     * @return
     */
    CareHistory queryCareHistoryByChId(String chId);

    /**
     * 保存处方及详情信息
     * @param careOrderFormDto
     * @return
     */
    int saveCareOrderItem(CareOrderFormDto careOrderFormDto);

    /**
     * 根据处方详情ID查询处方详情数据
     * @param itemId
     * @return
     */
    CareOrderItem queryCareOrderItemByItemId(String itemId);
    /**
     * 根据详情ID删除详情信息
     * @param itemId
     * @return
     */
    int deleteCareOrderItemByItemId(String itemId);

    /**
     * 完成就诊
     * @param regId
     * @return
     */
    int visitComplete(String regId);
}
