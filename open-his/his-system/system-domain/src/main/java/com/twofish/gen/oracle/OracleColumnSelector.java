package com.twofish.gen.oracle;

import com.twofish.gen.ColumnDefinition;
import com.twofish.gen.ColumnSelector;
import com.twofish.gen.GeneratorConfig;
import com.twofish.gen.TypeFormatter;

import java.util.Map;
import java.util.Set;

/**
 * oracle表信息查询
 */
public class OracleColumnSelector extends ColumnSelector {

	private static final TypeFormatter TYPE_FORMATTER = new OracleTypeFormatter();

	public OracleColumnSelector(GeneratorConfig generatorConfig) {
		super(generatorConfig);
	}

	/**
	 * 查询 字段名 类型 编码 是否为空 是否主键 默认值 权限 注释
	 * select
	 * utc.column_name as 字段名,utc.data_type 数据类型,utc.data_length 最大长度,
	 * CASE utc.nullable WHEN 'N' THEN '否' ELSE '是' END 可空,
	 * utc.data_default 默认值,ucc.comments 注释,UTC.table_name 表名,
	 * CASE UTC.COLUMN_NAME
	 * WHEN (select
	 * col.column_name
	 * from
	 * user_constraints con,user_cons_columns col
	 * where
	 * con.constraint_name=col.constraint_name and con.constraint_type='P'
	 * and col.table_name='DEMO')   THEN '是' ELSE '否' END AS 主键
	 * from
	 * user_tab_columns utc,user_col_comments ucc
	 * where
	 * utc.table_name = ucc.table_name
	 * and utc.column_name = ucc.column_name
	 * and utc.table_name = 'DEMO'
	 * order by
	 * column_id;
	 */
	@Override
	protected String getColumnInfoSQL(String tableName) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select ");
		sb.append(" utc.column_name as FIELD,utc.data_type TYPE,utc.data_length 最大长度, ");
		sb.append(" 		CASE utc.nullable WHEN 'N' THEN '否' ELSE '是' END 可空, ");
		sb.append(" utc.data_default 默认值,ucc.comments COMMENTS,UTC.table_name 表名, ");
		sb.append(" CASE UTC.COLUMN_NAME ");
		sb.append(" WHEN (select ");
		sb.append(" 		col.column_name ");
		sb.append(" 		from ");
		sb.append(" 		user_constraints con,user_cons_columns col ");
		sb.append(" 		where ");
		sb.append(" 	con.constraint_name=col.constraint_name and con.constraint_type='P' ");
		sb.append(" 		and col.table_name='"+tableName+"')   THEN 'true' ELSE 'false' END AS KEY ");
		sb.append(" 		from ");
		sb.append(" user_tab_columns utc,user_col_comments ucc ");
		sb.append(" 		where ");
		sb.append(" utc.table_name = ucc.table_name ");
		sb.append(" and utc.column_name = ucc.column_name ");
		sb.append(" and utc.table_name = '"+tableName+"' ");
		sb.append(" order by ");
		sb.append(" column_id ");
		return sb.toString();
	}
	
	@Override
	protected ColumnDefinition buildColumnDefinition(Map<String, Object> rowMap){
		Set<String> columnSet = rowMap.keySet();
		
		for (String columnInfo : columnSet) {
			rowMap.put(columnInfo.toUpperCase(), rowMap.get(columnInfo));
		}
		
		ColumnDefinition columnDefinition = new ColumnDefinition();
		
		columnDefinition.setColumnName((String)rowMap.get("FIELD"));

		columnDefinition.setIsIdentity(false);
		
		boolean isPk = "true".equalsIgnoreCase((String)rowMap.get("KEY"));
		columnDefinition.setIsPk(isPk);
		
		String type = (String)rowMap.get("TYPE");
		columnDefinition.setType(TYPE_FORMATTER.format(type));
		
		columnDefinition.setComment((String)rowMap.get("COMMENTS"));
		
		return columnDefinition;
	}
	

}
