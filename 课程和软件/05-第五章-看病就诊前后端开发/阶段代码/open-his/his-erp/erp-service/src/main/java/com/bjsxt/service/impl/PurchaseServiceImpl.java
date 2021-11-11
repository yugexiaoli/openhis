package com.bjsxt.service.impl;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.constants.Constants;
import com.bjsxt.domain.*;
import com.bjsxt.dto.PurchaseDto;
import com.bjsxt.dto.PurchaseFormDto;
import com.bjsxt.dto.PurchaseItemDto;
import com.bjsxt.mapper.InventoryLogMapper;
import com.bjsxt.mapper.MedicinesMapper;
import com.bjsxt.mapper.PurchaseItemMapper;
import com.bjsxt.mapper.PurchaseMapper;
import com.bjsxt.service.PurchaseService;
import com.bjsxt.utils.IdGeneratorSnowflake;
import com.bjsxt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
* @Author: 尚学堂 雷哥
*/

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Autowired
    private MedicinesMapper medicinesMapper;


    @Override
    public DataGridView listPurchasePage(PurchaseDto purchaseDto) {
        Page<Purchase> page=new Page<>(purchaseDto.getPageNum(),purchaseDto.getPageSize());
        QueryWrapper<Purchase> qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()),Purchase.COL_PROVIDER_ID,purchaseDto.getProviderId());
        qw.eq(StringUtils.isNotBlank(purchaseDto.getStatus()),Purchase.COL_STATUS,purchaseDto.getStatus());
        qw.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()),Purchase.COL_APPLY_USER_NAME,purchaseDto.getApplyUserName());
        qw.orderByDesc(Purchase.COL_CREATE_TIME);
        this.purchaseMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return this.purchaseMapper.selectById(purchaseId);
    }

    @Override
    public int doAudit(String purchaseId, SimpleUser simpleUser) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);//设置状态为待审核
        purchase.setApplyUserName(simpleUser.getUserName());
        purchase.setApplyUserId(Long.valueOf(simpleUser.getUserId().toString()));
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int doInvalid(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_5);//设置状态为作废
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditPass(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_3);//设置状态为审核通过
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditNoPass(String purchaseId, String auditMsg) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_4);//设置状态为审核不通过
        purchase.setAuditMsg(auditMsg);
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if(null!=purchaseId){
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            return purchaseItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 暂存   采购单的状态是Constants.STOCK_PURCHASE_STATUS_1
     * @param purchaseFormDto
     * @return
     */
    @Override
    public int addPurchaseAndItem(PurchaseFormDto purchaseFormDto) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseFormDto.getPurchaseDto().getPurchaseId());
        if(null!=purchase){
            //删除之前的数据
            this.purchaseMapper.deleteById(purchase.getPurchaseId());
            //删除之前的详情数据
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchase.getPurchaseId());
            this.purchaseItemMapper.delete(qw);
        }
        //保存采购单主表数据
        Purchase newPurchase=new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newPurchase);
        newPurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_1);//未提交状态
        newPurchase.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        newPurchase.setCreateTime(DateUtil.date());
        int a=this.purchaseMapper.insert(newPurchase);
        //保存详情数据
        for (PurchaseItemDto item : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem=new PurchaseItem();
            BeanUtil.copyProperties(item,purchaseItem);
            purchaseItem.setPurchaseId(newPurchase.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            this.purchaseItemMapper.insert(purchaseItem);
        }
        return a;
    }

    /**
     * 保存并提交审核  状态应该是Constants.STOCK_PURCHASE_STATUS_2
     * @param purchaseFormDto
     * @return
     */
    @Override
    public int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseFormDto.getPurchaseDto().getPurchaseId());
        if(null!=purchase){
            //删除之前的数据
            this.purchaseMapper.deleteById(purchase.getPurchaseId());
            //删除之前的详情数据
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchase.getPurchaseId());
            this.purchaseItemMapper.delete(qw);
        }
        //保存采购单主表数据
        Purchase newPurchase=new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newPurchase);
        newPurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);//待审核
        newPurchase.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        newPurchase.setCreateTime(DateUtil.date());
        //设置申请人和申请人的ID
        newPurchase.setApplyUserId(Long.valueOf(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserId().toString()));
        newPurchase.setApplyUserName(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        int a=this.purchaseMapper.insert(newPurchase);
        //保存详情数据
        for (PurchaseItemDto item : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem=new PurchaseItem();
            BeanUtil.copyProperties(item,purchaseItem);
            purchaseItem.setPurchaseId(newPurchase.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            this.purchaseItemMapper.insert(purchaseItem);
        }
        return a;
    }

    /**
     * 入库
     * 逻辑
     * 向stock_inventory_log表里面添加数据 并更新stock_medicines的库存
     * @param purchaseId
     * @param currentSimpleUser
     * @return
     */
    @Override
    public int doInventory(String purchaseId, SimpleUser currentSimpleUser) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        if(null!=purchase||purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
            //查询详情
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchase.getPurchaseId());
            List<PurchaseItem> purchaseItems = this.purchaseItemMapper.selectList(qw);
            for (PurchaseItem purchaseItem : purchaseItems) {
                InventoryLog inventoryLog=new InventoryLog();
                inventoryLog.setInventoryLogId(purchaseItem.getItemId());
                inventoryLog.setPurchaseId(purchaseItem.getPurchaseId());
                inventoryLog.setMedicinesId(purchaseItem.getMedicinesId());
                inventoryLog.setInventoryLogNum(purchaseItem.getPurchaseNumber());
                inventoryLog.setTradePrice(purchaseItem.getTradePrice());
                inventoryLog.setTradeTotalAmount(purchaseItem.getTradeTotalAmount());
                inventoryLog.setBatchNumber(purchaseItem.getBatchNumber());
                inventoryLog.setMedicinesName(purchaseItem.getMedicinesName());
                inventoryLog.setMedicinesType(purchaseItem.getMedicinesType());
                inventoryLog.setPrescriptionType(purchaseItem.getPrescriptionType());
                inventoryLog.setProducterId(purchaseItem.getProducterId());
                inventoryLog.setConversion(purchaseItem.getConversion());
                inventoryLog.setUnit(purchaseItem.getUnit());
                inventoryLog.setCreateTime(DateUtil.date());
                inventoryLog.setCreateBy(currentSimpleUser.getUserName());
                //保存数据
                inventoryLogMapper.insert(inventoryLog);

                //更新药品库存
                Medicines medicines=this.medicinesMapper.selectById(purchaseItem.getMedicinesId());
                medicines.setMedicinesStockNum(medicines.getMedicinesStockNum()+purchaseItem.getPurchaseNumber());
                medicines.setUpdateBy(currentSimpleUser.getUserName());
                this.medicinesMapper.updateById(medicines);
            }
            //入库成功  修改单据的状态为入库成功
            purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_6);//入库成功
            purchase.setStorageOptTime(DateUtil.date());
            purchase.setStorageOptUser(currentSimpleUser.getUserName());
            return this.purchaseMapper.updateById(purchase);
        }
        return -1;
    }
}
