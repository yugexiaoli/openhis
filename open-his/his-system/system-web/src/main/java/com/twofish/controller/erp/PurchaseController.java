package com.twofish.controller.erp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.constants.Constants;
import com.twofish.controller.BaseController;
import com.twofish.domain.Purchase;
import com.twofish.domain.PurchaseItem;
import com.twofish.dto.PurchaseDto;
import com.twofish.service.PurchaseService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * 采购单据信息接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "采购单据接口",tags = "采购单据接口")
@RequestMapping("/erp/purchase/")
public class PurchaseController extends BaseController {

    @Reference
    private PurchaseService purchaseService;

    /**
     * 分页查询所有采购入库列表信息
     * @param purchaseDto
     * @return
     */
    @GetMapping("listPurchaseForPage")
    @ApiOperation(value = "分页查询所有采购入库列表信息",notes = "分页查询所有采购入库列表信息")
    @HystrixCommand
    public AjaxResult listPurchaseForPage(PurchaseDto purchaseDto){
        DataGridView dataGridView = this.purchaseService.listPurchaseForPage(purchaseDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 分页查询所有待审核的入库信息
     * 状态：待审核
     * @param purchaseDto
     * @return
     */
    @GetMapping("listPurchasePendingForPage")
    @ApiOperation(value = "分页查询所有待审核的入库信息",notes = "分页查询所有待审核的入库信息")
    @HystrixCommand
    public AjaxResult listPurchasePendingForPage(PurchaseDto purchaseDto){
        purchaseDto.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        DataGridView dataGridView = this.purchaseService.listPurchaseForPage(purchaseDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }


    /**
     * 提交审核【根据采购单号】
     * 要求：状态必须是 1未提交、4审核失败
     * @param purchaseId
     * @return
     */
    @PostMapping("doAudit/{purchaseId}")
    @ApiOperation(value = "提交审核",notes = "提交审核")
    @Log(title = "提交审核",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult doAudit( @PathVariable("purchaseId") String purchaseId){
        Purchase purchase=this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1)||purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)){
            int result= this.purchaseService.doAudit(purchaseId,ShiroSecurityUtils.getCurrentSimpleUser());
            return AjaxResult.toAjax(result);
        }else {
            //不是1未提交、4审核失败
            return AjaxResult.fail("单据状态不是【未提交】或【审核失败】，提交失败");
        }
    }

    /**
     * 作废单据【根据采购单号】
     * 要求：状态必须是 1未提交、4审核失败
     * @param purchaseId
     * @return
     */
    @PostMapping("doInvalid/{purchaseId}")
    @ApiOperation(value = "作废单据",notes = "作废单据")
    @Log(title = "作废单据",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult doInvalid( @PathVariable("purchaseId") String purchaseId){
        Purchase purchase=this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1)||purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)){
            int result= this.purchaseService.doInvalid(purchaseId);
            return AjaxResult.toAjax(result);
        }else {
            //不是1未提交、4审核失败
            return AjaxResult.fail("单据状态不是【未提交】或【审核失败】，作废失败");
        }
    }

    /**
     * 采购单审核通过【根据采购单号】
     * 要求：状态必须是 2待审核
     * @param purchaseId
     * @return
     */
    @PostMapping("auditPass/{purchaseId}")
    @ApiOperation(value = "采购单审核通过",notes = "采购单审核通过")
    @Log(title = "采购单审核通过",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult auditPass( @PathVariable("purchaseId") String purchaseId){
        Purchase purchase=this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)){
            int result= this.purchaseService.auditPass(purchaseId);
            return AjaxResult.toAjax(result);
        }else {
            //不是 2待审核
            return AjaxResult.fail("单据状态不是【待审核】，审核失败");
        }
    }

    /**
     * 采购单审核不通过【根据采购单号】
     * 要求：状态必须是 2待审核
     * @param purchaseId
     * @return
     */
    @PostMapping("auditNoPass/{purchaseId}/{auditMsg}")
    @ApiOperation(value = "采购单审核不通过",notes = "采购单审核不通过")
    @Log(title = "采购单审核不通过",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult auditNoPass( @PathVariable("purchaseId") String purchaseId,@PathVariable("auditMsg") String auditMsg){
        Purchase purchase=this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)){
            int result= this.purchaseService.auditNoPass(purchaseId,auditMsg);
            return AjaxResult.toAjax(result);
        }else {
            //不是 2待审核
            return AjaxResult.fail("单据状态不是【待审核】，审核失败");
        }
    }

    /**
     * 根据采购单据ID查询采购详情信息
     * @param purchaseId
     * @return
     */
    @GetMapping("getPurchaseItemById/{purchaseId}")
    @ApiOperation(value = "根据采购单据ID查询采购详情信息",notes = "根据采购单据ID查询采购详情信息")
    @HystrixCommand
    public AjaxResult getPurchaseItemById(@PathVariable("purchaseId") String purchaseId){
        List<PurchaseItem> list = this.purchaseService.getPurchaseItemById(purchaseId);
        return AjaxResult.success(list);
    }





}
