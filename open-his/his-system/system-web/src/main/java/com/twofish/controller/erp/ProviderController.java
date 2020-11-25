package com.twofish.controller.erp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.controller.BaseController;
import com.twofish.dto.ProviderDto;
import com.twofish.service.ProviderService;
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
 * 供应商接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "供应商数据接口",tags = "供应商信息表数据接口")
@RequestMapping("/erp/provider/")
public class ProviderController  extends BaseController {

    @Reference
    private ProviderService providerService;

    /**
     * 分页查询供应商信息表数据
     * @param providerDto
     * @return
     */
    @GetMapping("listProviderForPage")
    @ApiOperation(value = "分页查询供应商信息表数据",notes = "供应商信息表数据分页")
    @HystrixCommand
    public AjaxResult listProviderForPage(ProviderDto providerDto){
        DataGridView dataGridView = this.providerService.listProviderForPage(providerDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加供应商信息表数据
     * @param providerDto
     * @return
     */
    @Log(title = "添加供应商信息表数据",businessType = BusinessType.INSERT)
    @PostMapping("addProvider")
    @ApiOperation(value = "添加供应商信息表数据",notes = "添加供应商信息表数据")
    @HystrixCommand
    public AjaxResult addProvider(@Validated ProviderDto providerDto){
        //设置添加人
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.addProvider(providerDto));
    }


    /**
     * 根据id查询供应商信息表数据
     * @param providerId
     * @return
     */
    @GetMapping("getProviderById/{providerId}")
    @ApiOperation(value = "根据id查询供应商信息表数据",notes = "根据id查询供应商信息表数据")
    @HystrixCommand
    public AjaxResult getProviderById(@PathVariable @Validated @NotNull(message = "供应商信息表id不能为空") Long providerId){
        return AjaxResult.success("查询成功",this.providerService.getProviderById(providerId));
    }

    /**
     * 更新供应商信息表数据
     * @param providerDto
     * @return
     */
    @Log(title = "更新供应商信息表数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateProvider")
    @ApiOperation(value = "更新供应商信息表数据数据",notes = "更新供应商信息表数据")
    @HystrixCommand
    public AjaxResult updateProvider(@Validated ProviderDto providerDto){
        //设置修改人
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.updateProvider(providerDto));
    }


    /**
     * 删除供应商信息表数据(可批量删除)
     * @param providerIds 供应商信息表数据id数组
     * @return
     */
    @Log(title = "删除供应商信息表数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteProviderByIds/{providerIds}")
    @ApiOperation(value = "根据id删除供应商信息表数据",notes = "根据id删除供应商信息表数据")
    @HystrixCommand
    public AjaxResult deleteProviderByIds(@PathVariable @Validated @NotEmpty(message = "供应商信息表id不能为空") Long[] providerIds){
        return AjaxResult.toAjax(this.providerService.deleteProviderByIds(providerIds));
    }



    /**
     * 查询所有可用的供应商信息表
     * @return
     */
    @GetMapping("selectAllProvider")
    @ApiOperation(value = "查询所有可用的供应商信息表",notes = "查询所有可用的供应商信息表")
    @HystrixCommand
    public AjaxResult selectAllProvider(){
        return AjaxResult.success("查询成功",this.providerService.selectAllProvider().getData());
    }

}
