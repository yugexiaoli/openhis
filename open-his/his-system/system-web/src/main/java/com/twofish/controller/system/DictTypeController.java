package com.twofish.controller.system;

import com.twofish.constants.Constants;
import com.twofish.constants.HttpStatus;
import com.twofish.domain.DictType;
import com.twofish.domain.Menu;
import com.twofish.domain.SimpleUser;
import com.twofish.dto.DicTypeDto;
import com.twofish.service.DictTypeService;
import com.twofish.service.MenuService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 字典类型接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "系统字典类型接口",tags = "字典类型接口")
@RequestMapping("/system/dict/type/")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     * @param dicTypeDto
     * @return
     */
    @GetMapping("listForPage")
    @ApiOperation(value = "分页查询字典类型",notes = "字典类型分页")
    public AjaxResult listForPage(DicTypeDto dicTypeDto){
        DataGridView dataGridView = this.dictTypeService.listpage(dicTypeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加字典类型
     * @param dicTypeDto
     * @return
     */
    @PostMapping("addDictType")
    @ApiOperation(value = "添加字典类型",notes = "添加字典类型")
    public AjaxResult addDictType(@Validated DicTypeDto dicTypeDto){
        //判断插入的字典类型是否重复
        if(!this.dictTypeService.checkdictypeuniq(dicTypeDto.getDictId(),dicTypeDto.getDictType())){
            //已存在类型
            return AjaxResult.fail("添加的字典类型["+dicTypeDto.getDictType()+"]已存在");
        }
        //设置添加人
        dicTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.insertdictype(dicTypeDto));
    }


    /**
     * 根据id查询字典类型
     * @param dictId
     * @return
     */
    @GetMapping("getOne/{dictId}")
    @ApiOperation(value = "根据id查询字典类型",notes = "根据id查询字典类型")
    public AjaxResult getOne(@PathVariable Long dictId){
        return AjaxResult.success("查询成功",this.dictTypeService.selectdictypebyid(dictId));
    }

    /**
     * 更新字典类型
     * @param dicTypeDto
     * @return
     */
    @PutMapping("updateDictType")
    @ApiOperation(value = "更新字典类型数据",notes = "更新字典类型数据")
    public AjaxResult getOne(@Validated DicTypeDto dicTypeDto){
        //判断插入的字典类型是否重复
        if(!this.dictTypeService.checkdictypeuniq(dicTypeDto.getDictId(),dicTypeDto.getDictType())){
            //已存在类型
           return AjaxResult.fail("修改的字典类型["+dicTypeDto.getDictType()+"]已存在");
        }
        //设置修改人
        dicTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.updatedictype(dicTypeDto));
    }


    /**
     * 删除字典类型(可批量删除)
     * @param dictIds 字典类型id数组
     * @return
     */
    @DeleteMapping("deleteDictTypeByIds/{dictIds}")
    @ApiOperation(value = "根据id删除字典类型",notes = "根据id删除字典类型")
    public AjaxResult deleteDictTypeByIds(@PathVariable Long[] dictIds){
        return AjaxResult.toAjax(this.dictTypeService.deletedictypebyids(dictIds));
    }


    /**
     * 查询所有字典类型数据
     * @return
     */
    @GetMapping("selectAllDictType")
    @ApiOperation(value = "查询所有字典类型数据",notes = "查询所有字典类型数据")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.dictTypeService.list().getData());
    }


    /**
     * 字典数据缓存同步到redis
     * @return
     */
    @GetMapping("dictCacheAsync")
    @ApiOperation(value = "字典数据缓存同步到redis",notes = "字典数据缓存同步到redis")
    public AjaxResult dictCacheAsync(){
        try {
            this.dictTypeService.dictCacheAsync();
            return AjaxResult.success();
        }catch (Exception e){
            log.error("缓存同步失败"+e.getMessage());
            return AjaxResult.error();
        }
    }



}
