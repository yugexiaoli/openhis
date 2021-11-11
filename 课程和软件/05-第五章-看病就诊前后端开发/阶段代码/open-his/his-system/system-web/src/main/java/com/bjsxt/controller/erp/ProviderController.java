package com.bjsxt.controller.erp;

import com.bjsxt.aspectj.annotation.Log;
import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.controller.BaseController;
import com.bjsxt.dto.ProviderDto;
import com.bjsxt.service.ProviderService;
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
 * @Description:供应商控制器
 */
@RestController
@RequestMapping("erp/provider")
public class ProviderController extends BaseController {


    @Reference//使用dubbo的引用
    private ProviderService providerService;

    /**
     * 分页查询
     */
    @GetMapping("listProviderForPage")
    @HystrixCommand
    public AjaxResult listProviderForPage(ProviderDto providerDto){
        DataGridView gridView = this.providerService.listProviderPage(providerDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addProvider")
    @HystrixCommand
    @Log(title = "添加供应商",businessType = BusinessType.INSERT)
    public AjaxResult addProvider(@Validated ProviderDto providerDto) {
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.addProvider(providerDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateProvider")
    @HystrixCommand
    @Log(title = "修改供应商",businessType = BusinessType.UPDATE)
    public AjaxResult updateProvider(@Validated ProviderDto providerDto) {
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.updateProvider(providerDto));
    }


    /**
     * 根据ID查询一个供应商信息
     */
    @GetMapping("getProviderById/{providerId}")
    @HystrixCommand
    public AjaxResult getProviderById(@PathVariable @Validated @NotNull(message = "供应商ID不能为空") Long providerId) {
        return AjaxResult.success(this.providerService.getOne(providerId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteProviderByIds/{providerIds}")
    @HystrixCommand
    @Log(title = "删除供应商",businessType = BusinessType.DELETE)
    public AjaxResult deleteProviderByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] providerIds) {
        return AjaxResult.toAjax(this.providerService.deleteProviderByIds(providerIds));
    }

    /**
     * 查询所有可用的供应商
     */
    @HystrixCommand
    @GetMapping("selectAllProvider")
    public AjaxResult selectAllProvider() {
        return AjaxResult.success(this.providerService.selectAllProvider());
    }

}
