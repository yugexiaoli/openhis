package com.twofish.controller.erp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.controller.BaseController;
import com.twofish.dto.InventoryLogDto;
import com.twofish.service.InventoryLogService;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * 库存表接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "库存表接口",tags = "库存表接口")
@RequestMapping("/erp/inventoryLog/")
public class InventoryLogController extends BaseController {

    @Reference
    private InventoryLogService inventoryLogService;

    /**
     * 分页查询库存表数据
     * @param inventoryLogDto
     * @return
     */
    @GetMapping("listInventoryLogForPage")
    @ApiOperation(value = "分页查询库存表数据",notes = "分页查询库存表数据")
    @HystrixCommand
    public AjaxResult listInventoryLogForPage(InventoryLogDto inventoryLogDto){
        DataGridView dataGridView = this.inventoryLogService.listStockInventoryLogForPage(inventoryLogDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }


}
