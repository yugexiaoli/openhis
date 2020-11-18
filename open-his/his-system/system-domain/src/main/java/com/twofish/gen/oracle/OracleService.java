package com.twofish.gen.oracle;

import com.twofish.gen.GeneratorConfig;
import com.twofish.gen.SQLService;
import com.twofish.gen.TableSelector;

public class OracleService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new OracleTableSelector(new OracleColumnSelector(generatorConfig), generatorConfig);
	}

}
