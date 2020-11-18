package com.twofish.gen.mysql;

import com.twofish.gen.GeneratorConfig;
import com.twofish.gen.SQLService;
import com.twofish.gen.TableSelector;

public class MySqlService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new MySqlTableSelector(new MySqlColumnSelector(generatorConfig), generatorConfig);
	}

}
