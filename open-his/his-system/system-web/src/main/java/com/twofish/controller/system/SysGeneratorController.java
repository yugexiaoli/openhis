package com.twofish.controller.system;

import com.twofish.dto.generator.GenDto;
import com.twofish.service.SysGeneratorService;
import com.twofish.vo.AjaxResult;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统代码生成控制器
 * @author ccy
 *
 */

@RestController
@AllArgsConstructor
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    private final SysGeneratorService sysGeneratorService;

    /**
     * 分页查询所有数据库表
     * @param dto
     * @return
     */
    @GetMapping("/queryPage")
    @ApiOperation(value = "分页查询所有数据库表",tags = "分页查询所有数据库表")
    public AjaxResult queryPage(GenDto dto) {
        return AjaxResult.success("查询成功",sysGeneratorService.queryPage(dto));
    }





}
