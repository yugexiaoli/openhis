package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.constants.Constants;
import com.twofish.domain.*;
import com.twofish.dto.PurchaseDto;
import com.twofish.dto.PurchaseFormDto;
import com.twofish.dto.PurchaseItemDto;
import com.twofish.mapper.InventoryLogMapper;
import com.twofish.mapper.MedicinesMapper;
import com.twofish.mapper.PurchaseItemMapper;
import com.twofish.mapper.PurchaseMapper;
import com.twofish.service.PurchaseService;
import com.twofish.utils.IdGeneratorSnowflake;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private PurchaseItemMapper purchaseItemMapper;
    @Resource
    private InventoryLogMapper inventoryLogMapper;
    @Resource
    private MedicinesMapper medicinesMapper;

    @Override
    public DataGridView listPurchaseForPage(PurchaseDto purchaseDto) {
        Page<Purchase> page = new Page<>(purchaseDto.getPageNum(), purchaseDto.getPageSize());
        QueryWrapper<Purchase> qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()),Purchase.COL_PROVIDER_ID,purchaseDto.getProviderId());
        qw.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()),Purchase.COL_APPLY_USER_NAME,purchaseDto.getApplyUserName());
        qw.eq(StringUtils.isNotBlank(purchaseDto.getStatus()),Purchase.COL_STATUS,purchaseDto.getStatus());
        qw.orderByDesc(Purchase.COL_CREATE_TIME);
        this.purchaseMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Purchase queryPurchaseById(String purchaseId) {
        return this.purchaseMapper.selectById(purchaseId);
    }

    @Override
    public int doAudit(String purchaseId, SimpleUser simpleUser) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        purchase.setApplyUserId(Long.valueOf( simpleUser.getUserId().toString()));
        purchase.setApplyUserName(simpleUser.getUserName());
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int doInvalid(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_5);
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditPass(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_3);
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditNoPass(String purchaseId, String auditMsg) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_4);
        purchase.setExamine(auditMsg);
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if(purchaseId!=null){
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            return this.purchaseItemMapper.selectList(qw);
        }
        return null;
    }

    /**
     * 入库【根据采购单号】
     * 单据状态设置为6入库成功， 入库操作人和时间要设定，并且要修改药品的库存
     * @param purchaseId
     * @return
     */
    @Override
    public int doInventory(String purchaseId,SimpleUser currentSimpleUser) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
        qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
        List<PurchaseItem> purchaseItems = this.purchaseItemMapper.selectList(qw);
        for (PurchaseItem item : purchaseItems) {
            Medicines medicines = this.medicinesMapper.selectById(item.getMedicinesId());
            InventoryLog inventoryLog = new InventoryLog();
            BeanUtil.copyProperties(item,inventoryLog);
            inventoryLog.setInventoryLogId(item.getItemId());
            inventoryLog.setInventoryLogNum(item.getPurchaseNumber());
            inventoryLog.setPrescriptionPrice(medicines.getPrescriptionPrice());
            inventoryLog.setProviderId(purchase.getProviderId());
            inventoryLog.setCreateBy(currentSimpleUser.getUserName());
            //设置单据状态和入库人入库时间
            purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_6);
            purchase.setStorageOptTime(new Date());
            purchase.setStorageOptUser(currentSimpleUser.getUserName());
            purchase.setUpdateBy(currentSimpleUser.getUserName());
            this.purchaseMapper.updateById(purchase);
            //修改药品的库存
            medicines.setMedicinesStockNum(medicines.getMedicinesStockNum()+item.getPurchaseNumber());
            this.medicinesMapper.updateById(medicines);
            this.inventoryLogMapper.insert(inventoryLog);
        }
        return 1;
    }

    /**
     * 暂存采购单和详情信息
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * @param purchaseFormDto
     * @param currentSimpleUser
     * @return
     */
    @Override
    public int addPurchase(PurchaseFormDto purchaseFormDto, SimpleUser currentSimpleUser) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        List<PurchaseItemDto> itemDtos = purchaseFormDto.getPurchaseItemDtos();
        if(purchase!=null){
            //已经有单据，做修改单据和详情,先删单据和详情后插入单据和详情，相当于重新暂存
             this.purchaseMapper.deleteById(purchaseId);
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            this.purchaseItemMapper.delete(qw);
            Purchase newpurchase = new Purchase();
            BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newpurchase);
            newpurchase.setCreateBy(purchase.getCreateBy());
            newpurchase.setStatus(purchase.getStatus());
            newpurchase.setUpdateBy(currentSimpleUser.getUserName());
            this.purchaseMapper.insert(newpurchase);
            for (PurchaseItemDto itemDto : itemDtos) {
                PurchaseItem purchaseItem = new PurchaseItem();
                BeanUtil.copyProperties(itemDto,purchaseItem);
                purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
                purchaseItem.setPurchaseId(purchaseId);
                this.purchaseItemMapper.insert(purchaseItem);
            }
            return 1;
        }
        //做添加单据，添加单据和详情
        Purchase newpurchase = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newpurchase);
        newpurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_1);
        newpurchase.setCreateBy(currentSimpleUser.getUserName());
        this.purchaseMapper.insert(newpurchase);
        System.out.println("PurchaseItemDtos==="+itemDtos);
        for (PurchaseItemDto itemDto : itemDtos) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(itemDto,purchaseItem);
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            purchaseItem.setPurchaseId(newpurchase.getPurchaseId());
            this.purchaseItemMapper.insert(purchaseItem);
        }
        return 1;
    }

    /**
     * 添加采购单和详情并提交审核
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * 单据状态要设置为2待审核，并且设置申请人id和名称
     * @param purchaseFormDto
     * @param currentSimpleUser
     * @return
     */
    @Override
    public int addPurchaseToAudit(PurchaseFormDto purchaseFormDto, SimpleUser currentSimpleUser) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        List<PurchaseItemDto> itemDtos = purchaseFormDto.getPurchaseItemDtos();
        if(purchase!=null){
            //已经有单据，做修改单据和详情,先删单据和详情后插入单据和详情，相当于重新暂存
            this.purchaseMapper.deleteById(purchaseId);
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            this.purchaseItemMapper.delete(qw);
            Purchase newpurchase = new Purchase();
            BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newpurchase);
            newpurchase.setCreateBy(purchase.getCreateBy());
            newpurchase.setUpdateBy(currentSimpleUser.getUserName());
            newpurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
            newpurchase.setApplyUserId(Long.valueOf(currentSimpleUser.getUserId().toString()));
            newpurchase.setApplyUserName(currentSimpleUser.getUserName());
            this.purchaseMapper.insert(newpurchase);
            for (PurchaseItemDto itemDto : itemDtos) {
                PurchaseItem purchaseItem = new PurchaseItem();
                BeanUtil.copyProperties(itemDto,purchaseItem);
                purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
                purchaseItem.setPurchaseId(purchaseId);
                 this.purchaseItemMapper.insert(purchaseItem);
            }
            return 1;
        }
        //做添加单据，添加单据和详情
        Purchase newpurchase = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(),newpurchase);
        newpurchase.setCreateBy(currentSimpleUser.getUserName());
        newpurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        newpurchase.setApplyUserId(Long.valueOf(currentSimpleUser.getUserId().toString()));
        newpurchase.setApplyUserName(currentSimpleUser.getUserName());
        this.purchaseMapper.insert(newpurchase);
        for (PurchaseItemDto itemDto : itemDtos) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(itemDto,purchaseItem);
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            purchaseItem.setPurchaseId(newpurchase.getPurchaseId());
             this.purchaseItemMapper.insert(purchaseItem);
        }
        return 1;
    }
}
