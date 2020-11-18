package com.twofish.gen.sqlserver;

import com.twofish.gen.GeneratorConfig;
import com.twofish.gen.SQLService;
import com.twofish.gen.TableSelector;

public class SqlServerService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new SqlServerTableSelector(new SqlServerColumnSelector(generatorConfig), generatorConfig);
	}

}
