package com.twofish.gen.postgresql;

import com.twofish.gen.GeneratorConfig;
import com.twofish.gen.SQLService;
import com.twofish.gen.TableSelector;

/**
 * @author tanghc
 */
public class PostgreSqlService implements SQLService {
    @Override
    public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
        return new PostgreSqlTableSelector(new PostgreSqlColumnSelector(generatorConfig), generatorConfig);
    }

}
