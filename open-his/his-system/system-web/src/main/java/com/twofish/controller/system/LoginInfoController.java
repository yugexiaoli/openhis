package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.LoginInfoDto;
import com.twofish.service.LoginInfoService;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 登录日志接口
 * @author ccy
 */
@RestController
@Log4j2
@Api(value = "登录日志接口",tags = "登录日志接口")
@RequestMapping("/system/loginInfo/")
public class LoginInfoController {
    @Resource
    private LoginInfoService LoginInfoService;

    /**
     * 分页查询登录日志信息
     * @param LoginInfoDto
     * @return
     */
    @ApiOperation(value = "分页查询登录日志信息",notes = "分页查询登录日志信息")
    @GetMapping("listForPage")
    public AjaxResult listForPage(LoginInfoDto LoginInfoDto){
        DataGridView dgv = this.LoginInfoService.listForPage(LoginInfoDto);
        return AjaxResult.success("查询成功",dgv.getData(),dgv.getTotal());
    }


    /**
     * 根据id删除登录日志信息
     * @param infoIds
     * @return
     */
    @Log(title = "根据id删除登录日志信息",businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据id删除登录日志信息",notes = "根据id删除登录日志信息")
    @DeleteMapping("deleteLoginInfoByIds/{infoIds}")
    public AjaxResult deleteLoginInfoByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(this.LoginInfoService.deleteLoginInfoByIds(infoIds));
    }

    /**
     * 清空所有登录日志
     * @return
     */
    @Log(title = "清空所有登录日志",businessType = BusinessType.OTHER)
    @ApiOperation(value = "清空所有登录日志",notes = "清空所有登录日志")
    @DeleteMapping("clearLoginInfo")
    public AjaxResult clearLoginInfo(){
        return AjaxResult.toAjax(this.LoginInfoService.clearLoginInfo());
    }


}
