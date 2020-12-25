package com.twofish.controller.erp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.constants.Constants;
import com.twofish.controller.BaseController;
import com.twofish.domain.Purchase;
import com.twofish.domain.PurchaseItem;
import com.twofish.dto.PurchaseDto;
import com.twofish.dto.PurchaseFormDto;
import com.twofish.service.PurchaseService;
import com.twofish.utils.IdGeneratorSnowflake;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 采购单据信息接口
 * @author ccy
 * @data: 2020-3-16
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

    /**********************************/

    /**
     * 生成采购单ID
     * @return
     */
    @GetMapping("generatePurchaseId")
    @ApiOperation(value = "生成采购单ID",tags = "生成采购单ID")
    public AjaxResult generatePurchaseId(){
        return AjaxResult.success(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CG));
    }


    /**
     * 暂存采购单和详情信息
     * 可能是添加也可能是修改
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * @param purchaseFormDto
     * @return
     */
    @PostMapping("addPurchase")
    @ApiOperation(value = "暂存采购单和详情信息",notes = "暂存采购单和详情信息")
    @Log(title = "暂存采购单和详情信息",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addPurchase(@RequestBody PurchaseFormDto purchaseFormDto){
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        if(!checkPurchase(purchaseFormDto)){
            //表示状态不是1未提交，或者4审核失败，不能修改
            return AjaxResult.fail("单据【"+purchaseId+"】的状态不是未提交，或者审核失败，不能修改");
        }
        //做修改或添加单据
        return AjaxResult.toAjax(this.purchaseService.addPurchase(purchaseFormDto,ShiroSecurityUtils.getCurrentSimpleUser()));
    }

    /**
     * 添加采购单和详情并提交审核
     * 可能是添加也可能是修改
     * 添加就做插入操作，修改就先把单据和详情数据删掉再插入
     * @param purchaseFormDto
     * @return
     */
    @PostMapping("addPurchaseToAudit")
    @ApiOperation(value = "添加采购单和详情并提交审核",notes = "添加采购单和详情并提交审核")
    @Log(title = "添加采购单和详情并提交审核",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addPurchaseToAudit(@RequestBody PurchaseFormDto purchaseFormDto){
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        if(!checkPurchase(purchaseFormDto)){
            //表示状态不是1未提交，或者4审核失败，不能修改
            return AjaxResult.fail("单据【"+purchaseId+"】的状态不是未提交，或者审核失败，不能提交审核");
        }
        //做修改或添加单据
        return AjaxResult.toAjax(this.purchaseService.addPurchaseToAudit(purchaseFormDto,ShiroSecurityUtils.getCurrentSimpleUser()));
    }


    /**
     * 根据采购单号查询采购单信息和详情信息
     * @param purchaseId
     * @return
     */
    @GetMapping("queryPurchaseAndItemByPurchaseId/{purchaseId}")
    @ApiOperation(value = "根据采购单号查询采购单信息和详情信息",notes = "根据采购单号查询采购单信息和详情信息")
    @HystrixCommand
    public AjaxResult queryPurchaseAndItemByPurchaseId(@PathVariable("purchaseId") String purchaseId){
        //查询采购单
        Purchase purchase = this.purchaseService.queryPurchaseById(purchaseId);
        if(null==purchase){
            //采购单不存在
            return AjaxResult.fail("采购单【"+purchaseId+"】不存在");
        }
        //采购单存在，继续查详情信息，并且将两个信息装到map集合返回出去
        List<PurchaseItem> purchaseItems = this.purchaseService.getPurchaseItemById(purchaseId);
        Map<String,Object> map=new HashMap<>();
        map.put("purchase",purchase);
        map.put("items",purchaseItems);
        return AjaxResult.success(map);
    }




    /**
     * 入库【根据采购单号】
     * 要求：1,状态必须是 3，审核通过才能入库
     *      2,要修改药品的库存
     * @param purchaseId
     * @return
     */
    @PostMapping("doInventory/{purchaseId}")
    @ApiOperation(value = "入库",notes = "入库")
    @Log(title = "入库",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult doInventory(@PathVariable("purchaseId") String purchaseId){
        Purchase purchase = this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_6)){
            //状态已经为入库成功，不能重复入库
            return AjaxResult.fail("不能重复入库");
        }else if (!purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
            //状态不是为审核通过，不能入库
            return AjaxResult.fail("状态不是为审核通过，不能入库");
        }else {
            //状态为3，审核通过，可入库
            return AjaxResult.toAjax(this.purchaseService.doInventory(purchaseId,ShiroSecurityUtils.getCurrentSimpleUser()));
        }
    }



    /**
     * 用于添加单据或修改单据判断：
     * 只能是添加或者修改单据才能进入暂存
     * @param purchaseFormDto
     * @return
     */
    public Boolean checkPurchase(PurchaseFormDto purchaseFormDto){
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = this.purchaseService.queryPurchaseById(purchaseId);
        if(purchase==null){
            //做添加
            return true;
        }
        //做修改，修改状态必须是1未提交，或者4审核失败，才能修改
        if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1)||purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)){
            return true;
        }
        return false; //表示状态不是1未提交，或者4审核失败，不能修改
    }



}
