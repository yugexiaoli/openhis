package com.twofish.service;

import com.twofish.domain.Purchase;
import com.twofish.domain.PurchaseItem;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.PurchaseDto;
import com.twofish.dto.PurchaseFormDto;
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


    /**
     * 入库【根据采购单号】
     * 状态设置为6 入库操作人和时间要设定，并且要修改药品的库存
     * @param purchaseId
     * @return
     */
    int doInventory(String purchaseId,SimpleUser currentSimpleUser);

    /**
     * 暂存采购单和详情信息
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * @param purchaseFormDto
     * @param currentSimpleUser
     * @return
     */
    int addPurchase(PurchaseFormDto purchaseFormDto, SimpleUser currentSimpleUser);

    /**
     * 添加采购单和详情并提交审核
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * 状态要设置为2待审核，并且设置申请人id和名称
     * @param purchaseFormDto
     * @param currentSimpleUser
     * @return
     */
    int addPurchaseToAudit(PurchaseFormDto purchaseFormDto, SimpleUser currentSimpleUser);
}
