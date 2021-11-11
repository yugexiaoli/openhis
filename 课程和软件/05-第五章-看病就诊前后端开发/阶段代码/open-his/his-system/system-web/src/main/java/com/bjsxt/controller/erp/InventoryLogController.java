package com.bjsxt.controller.erp;

import com.bjsxt.aspectj.annotation.Log;
import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.controller.BaseController;
import com.bjsxt.domain.InventoryLog;
import com.bjsxt.dto.InventoryLogDto;
import com.bjsxt.dto.MedicinesDto;
import com.bjsxt.service.InventoryLogService;
import com.bjsxt.service.MedicinesService;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import com.bjsxt.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Auther: 尚学堂 雷哥
 * @Description:入库记录控制器
 */
@RestController
@RequestMapping("erp/inventoryLog")
public class InventoryLogController extends BaseController {


    @Reference//使用dubbo的引用
    private InventoryLogService inventoryLogService;

    /**
     * 分页查询
     */
    @GetMapping("listInventoryLogForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(InventoryLogDto inventoryLogDto){
        DataGridView gridView = this.inventoryLogService.listInventoryLogPage(inventoryLogDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

}
