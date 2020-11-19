package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.CheckItemDto;
import com.twofish.service.CheckItemService;
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
 * 部门检查费用表接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "系统检查费用表数据接口",tags = "检查费用表数据接口")
@RequestMapping("/system/checkItem/")
public class CheckItemController {

    @Resource
    private CheckItemService CheckItemService;

    /**
     * 分页查询所有检查项目
     * @param checkItemDto
     * @return
     */
    @GetMapping("listCheckItemForPage")
    @ApiOperation(value = "分页查询所有检查项目",notes = "分页查询所有检查项目")
    public AjaxResult listCheckItemForPage(CheckItemDto checkItemDto){
        DataGridView dataGridView = this.CheckItemService.listCheckItemForPage(checkItemDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加检查费用表数据
     * @param checkItemDto
     * @return
     */
    @Log(title = "添加检查费用表数据",businessType = BusinessType.INSERT)
    @PostMapping("addCheckItem")
    @ApiOperation(value = "添加检查费用表数据",notes = "添加检查费用表数据")
    public AjaxResult addCheckItem(@Validated CheckItemDto checkItemDto){
        //设置添加人
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.CheckItemService.addCheckItem(checkItemDto));
    }


    /**
     * 根据id查询检查费用表数据
     * @param checkItemId
     * @return
     */
    @GetMapping("getCheckItemById/{checkItemId}")
    @ApiOperation(value = "根据id查询检查费用表数据",notes = "根据id查询检查费用表数据")
    public AjaxResult getSysCheckItemById(@PathVariable @Validated @NotNull(message = "检查费用表id不能为空") Long checkItemId){
        return AjaxResult.success("查询成功",this.CheckItemService.getCheckItemById(checkItemId));
    }

    /**
     * 更新检查费用表数据
     * @param checkItemDto
     * @return
     */
    @Log(title = "更新检查费用表数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateCheckItem")
    @ApiOperation(value = "更新检查费用表数据数据",notes = "更新检查费用表数据")
    public AjaxResult updateCheckItem(@Validated CheckItemDto checkItemDto){
        //设置修改人
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.CheckItemService.updateCheckItem(checkItemDto));
    }


    /**
     * 删除检查费用表数据(可批量删除)
     * @param checkItemIds 检查费用表数据id数组
     * @return
     */
    @Log(title = "删除检查费用表数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteCheckItemByIds/{checkItemIds}")
    @ApiOperation(value = "根据id删除检查费用表数据",notes = "根据id删除检查费用表数据")
    public AjaxResult deleteCheckItemByIds(@PathVariable @Validated @NotEmpty(message = "检查费用表id不能为空") Long[] checkItemIds){
        return AjaxResult.toAjax(this.CheckItemService.deleteCheckItemByIds(checkItemIds));
    }



    /**
     * 查询所有可用的检查费用表
     * @return
     */
    @GetMapping("selectAllCheckItem")
    @ApiOperation(value = "查询所有可用的检查费用表",notes = "查询所有可用的检查费用表")
    public AjaxResult selectAllCheckItem(){
        return AjaxResult.success("查询成功",this.CheckItemService.selectAllCheckItem().getData());
    }

}
