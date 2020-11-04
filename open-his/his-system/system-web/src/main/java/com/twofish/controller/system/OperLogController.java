package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.DicDataDto;
import com.twofish.dto.OperLogDto;
import com.twofish.service.DictDataService;
import com.twofish.service.OperLogService;
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
 * 操作日志接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "操作日志接口",tags = "操作日志接口")
@RequestMapping("/system/operLog/")
public class OperLogController {
    @Resource
    private OperLogService operLogService;

    /**
     * 分页查询操作日志信息
     * @param operLogDto
     * @return
     */
    @ApiOperation(value = "分页查询操作日志信息",notes = "分页查询操作日志信息")
    @GetMapping("listForPage")
    public AjaxResult listForPage(OperLogDto operLogDto){
        DataGridView dgv = this.operLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功",dgv.getData(),dgv.getTotal());
    }

    /**
     * 根据id删除操作日志信息
     * @param infoIds
     * @return
     */
    @Log(title = "根据id删除操作日志信息",businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据id删除操作日志信息",notes = "根据id删除操作日志信息")
    @GetMapping("deleteOperLogByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(this.operLogService.deleteOperLogByIds(infoIds));
    }

    /**
     * 清空所有操作日志
     * @return
     */
    @Log(title = "清空所有操作日志",businessType = BusinessType.CLEAN)
    @ApiOperation(value = "清空所有操作日志",notes = "清空所有操作日志")
    @GetMapping("clearAllOperLog")
    public AjaxResult clearAllOperLog(){
        return AjaxResult.toAjax(this.operLogService.clearAllOperLog());
    }


}
