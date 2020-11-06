package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.DeptDto;
import com.twofish.service.DeptService;
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
 * 部门科室接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "系统科室数据接口",tags = "科室数据接口")
@RequestMapping("/system/dept/")
public class DeptController {

    @Resource
    private DeptService deptService;

    /**
     * 分页查询科室数据
     * @param deptDto
     * @return
     */
    @GetMapping("listDeptForPage")
    @ApiOperation(value = "分页查询科室数据",notes = "科室数据分页")
    public AjaxResult listDeptForPage(DeptDto deptDto){
        DataGridView dataGridView = this.deptService.listDeptForPage(deptDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加科室数据
     * @param deptDto
     * @return
     */
    @Log(title = "添加科室数据",businessType = BusinessType.INSERT)
    @PostMapping("addDept")
    @ApiOperation(value = "添加科室数据",notes = "添加科室数据")
    public AjaxResult addDept(@Validated DeptDto deptDto){
        //设置添加人
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.addDept(deptDto));
    }


    /**
     * 根据id查询科室数据
     * @param deptId
     * @return
     */
    @GetMapping("getDeptById/{deptId}")
    @ApiOperation(value = "根据id查询科室数据",notes = "根据id查询科室数据")
    public AjaxResult getDeptById(@PathVariable @Validated @NotNull(message = "科室id不能为空") Long deptId){
        return AjaxResult.success("查询成功",this.deptService.getDeptById(deptId));
    }

    /**
     * 更新科室数据
     * @param deptDto
     * @return
     */
    @Log(title = "更新科室数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateDept")
    @ApiOperation(value = "更新科室数据数据",notes = "更新科室数据")
    public AjaxResult updateDept(@Validated DeptDto deptDto){
        //设置修改人
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.updateDept(deptDto));
    }


    /**
     * 删除科室数据(可批量删除)
     * @param deptIds 科室数据id数组
     * @return
     */
    @Log(title = "删除科室数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteDeptByIds/{deptIds}")
    @ApiOperation(value = "根据id删除科室数据",notes = "根据id删除科室数据")
    public AjaxResult deleteDeptByIds(@PathVariable @Validated @NotEmpty(message = "科室id不能为空") Long[] deptIds){
        return AjaxResult.toAjax(this.deptService.deleteDeptByIds(deptIds));
    }



    /**
     * 查询所有可用的科室
     * @return
     */
    @GetMapping("selectAllDept")
    @ApiOperation(value = "查询所有可用的科室",notes = "查询所有可用的科室")
    public AjaxResult selectAllDept(){
        return AjaxResult.success("查询成功",this.deptService.selectAllDept().getData());
    }

}
