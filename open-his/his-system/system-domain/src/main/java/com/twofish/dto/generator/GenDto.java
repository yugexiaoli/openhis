package com.twofish.dto.generator;


import com.twofish.vo.BaseDto;
import lombok.Data;

/**
 * @author ccy
 *
 * 代码生成配置dto类
 *
 */
@Data
public class GenDto extends BaseDto {
    /**
     * 包名
     */
    private String packageName;
    /**
     * 作者
     */
    private String author;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表备注
     */
    private String comments;
}