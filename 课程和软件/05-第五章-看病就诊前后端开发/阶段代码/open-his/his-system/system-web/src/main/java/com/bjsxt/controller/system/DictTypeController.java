package com.bjsxt.controller.system;

import com.bjsxt.aspectj.annotation.Log;
import com.bjsxt.aspectj.enums.BusinessType;
import com.bjsxt.dto.DictTypeDto;
import com.bjsxt.service.DictTypeService;
import com.bjsxt.utils.ShiroSecurityUtils;
import com.bjsxt.vo.AjaxResult;
import com.bjsxt.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Auther: 尚学堂 雷哥
 * @Description:数据字典信息
 */
@RestController
@RequestMapping("system/dict/type")
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DictTypeDto dictTypeDto){
        DataGridView gridView = this.dictTypeService.listPage(dictTypeDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addDictType")
    @Log(title = "添加字典类型",businessType = BusinessType.INSERT)
    public AjaxResult addDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("新增字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.insert(dictTypeDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateDictType")
    @Log(title = "修改字典类型",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("修改字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.update(dictTypeDto));
    }


    /**
     * 根据ID查询一个字典信息
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getDictType(@PathVariable @Validated @NotNull(message = "字典ID不能为空") Long dictId) {
        return AjaxResult.success(this.dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteDictTypeByIds/{dictIds}")
    @Log(title = "删除字典类型",businessType = BusinessType.DELETE)
    public AjaxResult updateDictType(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] dictIds) {
        return AjaxResult.toAjax(this.dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("selectAllDictType")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.dictTypeService.list().getData());
    }


    /**
     * 同步字典数据到redis
     */
    @GetMapping("dictCacheAsync")
    @Log(title = "同步字典数据到redis",businessType = BusinessType.OTHER)
    public AjaxResult dictCacheAsync(){
        try {
            this.dictTypeService.dictCacheAsync();
            return AjaxResult.success();
        }catch (Exception e){
            System.out.println(e);
            return AjaxResult.error();
        }
    }


}
