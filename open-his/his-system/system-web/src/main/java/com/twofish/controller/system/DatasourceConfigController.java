package com.twofish.controller.system;


import com.twofish.domain.DatasourceConfig;
import com.twofish.gen.*;
import com.twofish.service.impl.DatasourceConfigService;
import com.twofish.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tanghc
 */
@RestController
@RequestMapping("datasource")
public class DatasourceConfigController {

    @Autowired
    private DatasourceConfigService datasourceConfigService;

    @RequestMapping("/add")
    public AjaxResult add(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.insert(datasourceConfig);
        return AjaxResult.success();
    }

    @RequestMapping("/list")
    public AjaxResult list() {
        List<DatasourceConfig> datasourceConfigList = datasourceConfigService.listAll();
        return AjaxResult.success(datasourceConfigList);
    }

    @RequestMapping("/update")
    public AjaxResult update(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.update(datasourceConfig);
        return AjaxResult.success();
    }

    @RequestMapping("/del")
    public AjaxResult del(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.delete(datasourceConfig);
        return AjaxResult.success();
    }

    @RequestMapping("/table/{id}")
    public AjaxResult listTable(@PathVariable("id") int id) {
        DatasourceConfig dataSourceConfig = datasourceConfigService.getById(id);
        GeneratorConfig generatorConfig = GeneratorConfig.build(dataSourceConfig);
        SQLService service = SQLServiceFactory.build(generatorConfig);
        List<TableDefinition> list = service.getTableSelector(generatorConfig).getSimpleTableDefinitions();
        return AjaxResult.success(list);
    }


    @RequestMapping("/test")
    public AjaxResult test(@RequestBody DatasourceConfig datasourceConfig) {
        String error = DBConnect.testConnection(GeneratorConfig.build(datasourceConfig));
        if (error != null) {
            return AjaxResult.error(error);
        }
        return AjaxResult.success();
    }

    @RequestMapping("/dbtype")
    public AjaxResult dbType(@RequestBody DatasourceConfig datasourceConfig) {
        List<DbTypeShow> dbTypeShowList = Stream.of(DbType.values())
                .map(dbType -> new DbTypeShow(dbType.getDisplayName(), dbType.getType()))
                .collect(Collectors.toList());
        return AjaxResult.success(dbTypeShowList);
    }

    private static class DbTypeShow {
        private String label;
        private Integer dbType;

        public DbTypeShow(String label, Integer dbType) {
            this.label = label;
            this.dbType = dbType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getDbType() {
            return dbType;
        }

        public void setDbType(Integer dbType) {
            this.dbType = dbType;
        }
    }


}
