package com.twofish.controller.erp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.controller.BaseController;
import com.twofish.dto.MedicinesDto;
import com.twofish.service.MedicinesService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 *
 * 部门药品信息表接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "药品信息表数据接口",tags = "药品信息表数据接口")
@RequestMapping("/erp/medicines/")
public class MedicinesController  extends BaseController {

    @Reference
    private MedicinesService medicinesService;

    /**
     * 分页查询药品信息表数据
     * @param medicinesDto
     * @return
     */
    @GetMapping("listMedicinesForPage")
    @ApiOperation(value = "分页查询药品信息表数据",notes = "药品信息表数据分页")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(MedicinesDto medicinesDto){
        DataGridView dataGridView = this.medicinesService.listMedicinesForPage(medicinesDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加药品信息表数据
     * @param medicinesDto
     * @return
     */
    @Log(title = "添加药品信息表数据",businessType = BusinessType.INSERT)
    @PostMapping("addMedicines")
    @ApiOperation(value = "添加药品信息表数据",notes = "添加药品信息表数据")
    @HystrixCommand
    public AjaxResult addMedicines(@Validated MedicinesDto medicinesDto){
        //设置添加人
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.medicinesService.addMedicines(medicinesDto));
    }


    /**
     * 根据id查询药品信息表数据
     * @param medicinesId
     * @return
     */
    @GetMapping("getMedicinesById/{medicinesId}")
    @ApiOperation(value = "根据id查询药品信息表数据",notes = "根据id查询药品信息表数据")
    @HystrixCommand
    public AjaxResult getMedicinesById(@PathVariable @Validated @NotNull(message = "药品信息表id不能为空") Long medicinesId){
        return AjaxResult.success("查询成功",this.medicinesService.getMedicinesById(medicinesId));
    }

    /**
     * 更新药品信息表数据
     * @param medicinesDto
     * @return
     */
    @Log(title = "更新药品信息表数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateMedicines")
    @ApiOperation(value = "更新药品信息表数据数据",notes = "更新药品信息表数据")
    @HystrixCommand
    public AjaxResult updateMedicines(@Validated MedicinesDto medicinesDto){
        //设置修改人
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.medicinesService.updateMedicines(medicinesDto));
    }


    /**
     * 删除药品信息表数据(可批量删除)
     * @param medicinesIds 药品信息表数据id数组
     * @return
     */
    @Log(title = "删除药品信息表数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteMedicinesByIds/{medicinesIds}")
    @ApiOperation(value = "根据id删除药品信息表数据",notes = "根据id删除药品信息表数据")
    @HystrixCommand
    public AjaxResult deleteMedicinesByIds(@PathVariable @Validated @NotEmpty(message = "药品信息表id不能为空") Long[] medicinesIds){
        return AjaxResult.toAjax(this.medicinesService.deleteMedicinesByIds(medicinesIds));
    }



    /**
     * 查询所有可用的药品信息表
     * @return
     */
    @GetMapping("selectAllMedicines")
    @ApiOperation(value = "查询所有可用的药品信息表",notes = "查询所有可用的药品信息表")
    @HystrixCommand
    public AjaxResult selectAllMedicines(){
        return AjaxResult.success("查询成功",this.medicinesService.selectAllMedicines().getData());
    }


    /**
     * 调整药品库存
     * @param
     * @return
     */
    @Log(title = "调整药品库存",businessType = BusinessType.UPDATE)
    @PostMapping("updateMedicinesStorage/{medicinesId}/{medicinesStockNum}")
    @ApiOperation(value = "调整药品库存",notes = "调整药品库存")
    @HystrixCommand
    public AjaxResult updateMedicinesStorage( @PathVariable @Validated @NotNull(message = "药品id不能为空") Long medicinesId ,@PathVariable @Validated @NotNull(message = "药品库存量不能为空")  Long medicinesStockNum){
       return AjaxResult.toAjax(medicinesService.updateMedicinesStorage(medicinesId,medicinesStockNum));
    }

    /**
     * 根据药品名称模糊查询药品信息
     *
     */
    @GetMapping("searchMedicinesByName/{search}")
    @ApiOperation(value = "根据药品名称模糊查询药品信息",notes = "根据药品名称模糊查询药品信息")
    @HystrixCommand
    public AjaxResult searchMedicinesByName(@PathVariable String search){
        return AjaxResult.success(this.medicinesService.queryMedicinesByName(search));
    }

}
