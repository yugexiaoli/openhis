package com.twofish.service;

import com.twofish.domain.Purchase;
import com.twofish.domain.PurchaseItem;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.PurchaseDto;
import com.twofish.vo.DataGridView;

import java.util.List;

public interface PurchaseService {

    /**
     * 分页查询采购单据
     * @param purchaseDto
     * @return
     */
    DataGridView listPurchaseForPage(PurchaseDto purchaseDto);

    /**
     * 根据id查询采购单据
     * @param purchaseId
     * @return
     */
    Purchase queryPurchaseById(String purchaseId);

    /**
     * 提交审核
     * 要求：设置申请人id和名称
     * @param purchaseId
     * @param simpleUser
     * @return
     */
    int doAudit(String purchaseId, SimpleUser simpleUser);

    /**
     * 作废审核
     * @param purchaseId
     * @return
     */
    int doInvalid(String purchaseId);

    /**
     * 采购单审核通过
     * @param purchaseId
     * @return
     */
    int auditPass(String purchaseId);

    /**
     * 采购单审核不通过
     * @param purchaseId
     * @param auditMsg
     * @return
     */
    int auditNoPass(String purchaseId, String auditMsg);

    /**
     * 根据采购单据ID查询采购详情信息
     * @param purchaseId
     * @return
     */
    List<PurchaseItem> getPurchaseItemById(String purchaseId);


}
