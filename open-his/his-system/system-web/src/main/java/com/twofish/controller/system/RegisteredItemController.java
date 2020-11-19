package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.RegisteredItemDto;
import com.twofish.service.RegisteredItemService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 *
 * 挂号费用接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "挂号费用接口",tags = "挂号费用接口")
@RequestMapping("/system/registeredItem/")
public class RegisteredItemController {

    @Resource
    private RegisteredItemService registeredItemService;

    /**
     * 分页查询所有挂号费用列表
     * @param registeredItemDto
     * @return
     */
    @GetMapping("listRegisteredItemForPage")
    @ApiOperation(value = "分页查询所有挂号费用列表",notes = "分页查询所有挂号费用列表")
    public AjaxResult listRegisteredItemForPage(RegisteredItemDto registeredItemDto){
        DataGridView dataGridView = this.registeredItemService.listRegisteredItemForPage(registeredItemDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加挂号费用
     * @param registeredItemDto
     * @return
     */
    @Log(title = "添加挂号费用",businessType = BusinessType.INSERT)
    @PostMapping("addRegisteredItem")
    @ApiOperation(value = "添加挂号费用",notes = "添加挂号费用")
    public AjaxResult addRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        //设置添加人
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.registeredItemService.addRegisteredItem(registeredItemDto));
    }


    /**
     * 根据ID查询挂号费用
     * @param registeredItemId
     * @return
     */
    @GetMapping("getRegisteredItemById/{registeredItemId}")
    @ApiOperation(value = "根据ID查询挂号费用",notes = "根据ID查询挂号费用")
    public AjaxResult getRegisteredItemById(@PathVariable @Validated @NotNull(message = "挂号费用id不能为空") Long registeredItemId){
        return AjaxResult.success("查询成功",this.registeredItemService.getRegisteredItemById(registeredItemId));
    }

    /**
     * 修改挂号信息
     * @param registeredItemDto
     * @return
     */
    @Log(title = "修改挂号信息",businessType = BusinessType.UPDATE)
    @PutMapping("updateRegisteredItem")
    @ApiOperation(value = "修改挂号信息",notes = "修改挂号信息")
    public AjaxResult updateRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        //设置修改人
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.registeredItemService.updateRegisteredItem(registeredItemDto));
    }


    /**
     * 根据ID删除挂号费用(可批量删除)
     * @param registeredItemIds 数据id数组
     * @return
     */
    @Log(title = "根据ID删除挂号费用",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteRegisteredItemByIds/{registeredItemIds}")
    @ApiOperation(value = "根据ID删除挂号费用",notes = "根据ID删除挂号费用")
    public AjaxResult deleteRegisteredItemByIds(@PathVariable @Validated @NotEmpty(message = "挂号费用id不能为空") Long[] registeredItemIds){
        return AjaxResult.toAjax(this.registeredItemService.deleteRegisteredItemByIds(registeredItemIds));
    }



    /**
     * 查询所有可用的挂号项目
     * @return
     */
    @GetMapping("selectAllRegisteredItem")
    @ApiOperation(value = "查询所有可用的挂号项目",notes = "查询所有可用的挂号项目")
    public AjaxResult selectAllRegisteredItem(){
        return AjaxResult.success("查询成功",this.registeredItemService.selectAllRegisteredItem().getData());
    }

}
