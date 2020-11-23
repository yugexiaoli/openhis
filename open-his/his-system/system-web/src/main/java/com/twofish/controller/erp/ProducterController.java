package com.twofish.controller.erp;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.ProducterDto;
import com.twofish.service.ProducterService;
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
 * 生产厂家表接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "系统生产厂家表数据接口",tags = "生产厂家表数据接口")
@RequestMapping("/erp/producter/")
public class ProducterController {

    @Reference
    private ProducterService producterService;

    /**
     * 分页查询生产厂家表数据
     * @param producterDto
     * @return
     */
    @GetMapping("listProducterForPage")
    @ApiOperation(value = "分页查询生产厂家表数据",notes = "生产厂家表数据分页")
    public AjaxResult listProducterForPage(ProducterDto producterDto){
        DataGridView dataGridView = this.producterService.listProducterForPage(producterDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加生产厂家表数据
     * @param producterDto
     * @return
     */
    @Log(title = "添加生产厂家表数据",businessType = BusinessType.INSERT)
    @PostMapping("addProducter")
    @ApiOperation(value = "添加生产厂家表数据",notes = "添加生产厂家表数据")
    public AjaxResult addProducter(@Validated ProducterDto producterDto){
        //设置添加人
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.addProducter(producterDto));
    }


    /**
     * 根据id查询生产厂家表数据
     * @param producterId
     * @return
     */
    @GetMapping("getProducterById/{producterId}")
    @ApiOperation(value = "根据id查询生产厂家表数据",notes = "根据id查询生产厂家表数据")
    public AjaxResult getProducterById(@PathVariable @Validated @NotNull(message = "生产厂家表id不能为空") Long producterId){
        return AjaxResult.success("查询成功",this.producterService.getProducterById(producterId));
    }

    /**
     * 更新生产厂家表数据
     * @param producterDto
     * @return
     */
    @Log(title = "更新生产厂家表数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateProducter")
    @ApiOperation(value = "更新生产厂家表数据数据",notes = "更新生产厂家表数据")
    public AjaxResult updateProducter(@Validated ProducterDto producterDto){
        //设置修改人
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.updateProducter(producterDto));
    }


    /**
     * 删除生产厂家表数据(可批量删除)
     * @param producterIds 生产厂家表数据id数组
     * @return
     */
    @Log(title = "删除生产厂家表数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteProducterByIds/{producterIds}")
    @ApiOperation(value = "根据id删除生产厂家表数据",notes = "根据id删除生产厂家表数据")
    public AjaxResult deleteProducterByIds(@PathVariable @Validated @NotEmpty(message = "生产厂家表id不能为空") Long[] producterIds){
        return AjaxResult.toAjax(this.producterService.deleteProducterByIds(producterIds));
    }



    /**
     * 查询所有可用的生产厂家表
     * @return
     */
    @GetMapping("selectAllProducter")
    @ApiOperation(value = "查询所有可用的生产厂家表",notes = "查询所有可用的生产厂家表")
    public AjaxResult selectAllProducter(){
        return AjaxResult.success("查询成功",this.producterService.selectAllProducter().getData());
    }

}
