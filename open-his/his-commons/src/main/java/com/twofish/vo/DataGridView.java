package com.twofish.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 表格数据传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-domain-DataGridView")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView implements Serializable {
    @ApiModelProperty(value="数据总条数")
    private Long total;

    @ApiModelProperty(value="返回数据")
    private List<?> data;
}
