package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.domain.DictData;
import com.twofish.dto.DicDataDto;
import com.twofish.service.DictDataService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 字典数据接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "系统字典数据接口",tags = "字典数据接口")
@RequestMapping("/system/dict/data/")
public class DictDateController {

    @Resource
    private DictDataService dictDataService;

    /**
     * 分页查询字典数据
     * @param dicDataDto
     * @return
     */
    @GetMapping("listForPage")
    @ApiOperation(value = "分页查询字典数据",notes = "字典数据分页")
    public AjaxResult listForPage(DicDataDto dicDataDto){
        DataGridView dataGridView = this.dictDataService.listpage(dicDataDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加字典数据
     * @param dicDataDto
     * @return
     */
    @Log(title = "添加字典数据",businessType = BusinessType.INSERT)
    @PostMapping("addDictData")
    @ApiOperation(value = "添加字典数据",notes = "添加字典数据")
    public AjaxResult addDictData(@Validated DicDataDto dicDataDto){
        //设置添加人
        dicDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictDataService.insertdictdata(dicDataDto));
    }


    /**
     * 根据id查询字典数据
     * @param dictCode
     * @return
     */
    @GetMapping("getOne/{dictCode}")
    @ApiOperation(value = "根据id查询字典数据",notes = "根据id查询字典数据")
    public AjaxResult getOne(@PathVariable @Validated @NotNull(message = "字典id不能为空") Long dictCode){
        return AjaxResult.success("查询成功",this.dictDataService.querybyid(dictCode));
    }

    /**
     * 更新字典数据
     * @param dicDataDto
     * @return
     */
    @Log(title = "更新字典数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateDictData")
    @ApiOperation(value = "更新字典数据数据",notes = "更新字典数据")
    public AjaxResult getOne(@Validated DicDataDto dicDataDto){
        //设置修改人
        dicDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictDataService.updatedictdata(dicDataDto));
    }


    /**
     * 删除字典数据(可批量删除)
     * @param dictCodeIds 字典数据id数组
     * @return
     */
    @Log(title = "删除字典数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteDictDataByIds/{dictCodeIds}")
    @ApiOperation(value = "根据id删除字典数据",notes = "根据id删除字典数据")
    public AjaxResult deleteDictDataByIds(@PathVariable @Validated @NotEmpty(message = "字典id不能为空") Long[] dictCodeIds){
        return AjaxResult.toAjax(this.dictDataService.deleteDictDataByIds(dictCodeIds));
    }


    /**
     * 根据类型查询所有字典数据数据(去数据库里查)
     * @return
     */
    @GetMapping("getDataByType/{dictType}")
    @ApiOperation(value = "根据类型查询所有字典数据数据",notes = "根据类型查询所有字典数据数据")
    public AjaxResult selectAllDictData(@PathVariable @Validated @NotNull(message = "字典类型不能为空") String dictType){
        return AjaxResult.success(dictDataService.querybydicttype(dictType));
    }


    /**
     * 根据字典类型去redis中取字典数据（去redis查）
     * @return
     */
    @GetMapping("selectDicDataBydictType/{dictType}")
    @ApiOperation(value = "根据字典类型去redis中取字典数据",notes = "根据字典类型去redis中取字典数据")
    public AjaxResult selectDicDataBydictType(@PathVariable @Validated @NotNull(message = "字典类型不能为空") String dictType){
        try {
            return AjaxResult.success("获取成功",this.dictDataService.selectDicDataBydictType(dictType));
        }catch (Exception e){
            log.error("从redis取字典缓存失败"+e.getMessage());
            return AjaxResult.error();
        }
    }





}
