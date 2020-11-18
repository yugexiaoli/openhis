package com.twofish.controller.system;

import com.twofish.domain.DatasourceConfig;
import com.twofish.gen.GeneratorConfig;
import com.twofish.generator.GeneratorParam;
import com.twofish.service.impl.DatasourceConfigService;
import com.twofish.service.impl.GeneratorService;
import com.twofish.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("generate")
public class GeneratorController {

    @Autowired
    private DatasourceConfigService datasourceConfigService;

    @Autowired
    private GeneratorService generatorService;

    /**
     * 生成代码
     *
     * @param generatorParam 生成参数
     * @return 返回代码内容
     */
    @RequestMapping("/code")
    public AjaxResult code(@RequestBody GeneratorParam generatorParam) {
        int datasourceConfigId = generatorParam.getDatasourceConfigId();
        DatasourceConfig datasourceConfig = datasourceConfigService.getById(datasourceConfigId);
        GeneratorConfig generatorConfig = GeneratorConfig.build(datasourceConfig);
        return AjaxResult.success(generatorService.generate(generatorParam, generatorConfig));
    }

}
