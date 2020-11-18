package com.twofish.controller.system;


import com.twofish.domain.TemplateConfig;
import com.twofish.service.impl.TemplateConfigService;
import com.twofish.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("template")
public class TemplateConfigController {

    @Autowired
    private TemplateConfigService templateConfigService;

    @RequestMapping("/add")
    public AjaxResult add(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.insert(templateConfig);
        return AjaxResult.success(templateConfig);
    }

    @RequestMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") int id) {
        return AjaxResult.success(templateConfigService.getById(id));
    }

    @RequestMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(templateConfigService.listAll());
    }

    @RequestMapping("/update")
    public AjaxResult update(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.update(templateConfig);
        return AjaxResult.success();
    }

    @RequestMapping("/del")
    public AjaxResult del(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.delete(templateConfig);
        return AjaxResult.success();
    }

}
