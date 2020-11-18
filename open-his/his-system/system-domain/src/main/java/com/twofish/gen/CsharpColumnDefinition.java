package com.twofish.gen;

import com.twofish.gen.converter.ColumnTypeConverter;
import com.twofish.gen.converter.CsharpColumnTypeConverter;
import com.twofish.utils.FieldUtil;

/**
 * 提供C# Velocity变量
 * @author tanghc
 */
public class CsharpColumnDefinition extends ColumnDefinition {

    private static final ColumnTypeConverter COLUMN_TYPE_CONVERTER = new CsharpColumnTypeConverter();

    public String getField() {
        return FieldUtil.underlineFilter(getColumnName());
    }

    public String getProperty() {
        return FieldUtil.upperFirstLetter(getField());
    }

    @Override
    public ColumnTypeConverter getColumnTypeConverter() {
        return COLUMN_TYPE_CONVERTER;
    }
}
