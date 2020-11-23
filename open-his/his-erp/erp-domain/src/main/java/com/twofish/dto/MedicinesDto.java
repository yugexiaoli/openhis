package com.twofish.dto;

import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 药品信息表Dto传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-dto-MedicinesDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class MedicinesDto extends BaseDto {
    /**
     *药品id
     */
    @ApiModelProperty(value="")
    private Long medicinesId;
    /**
     * 药品编号
     */
    @ApiModelProperty(value="药品编号")
    private String medicinesNumber;
    /**
     * 药品名称
     */
    @NotBlank(message = "药品名称不能为空")
    @ApiModelProperty(value="药品名称")
    private String medicinesName;
    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @ApiModelProperty(value="药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;
    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @ApiModelProperty(value="处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;
    /**
     * 处方价格
     */
    @ApiModelProperty(value="处方价格")
    private BigDecimal prescriptionPrice;
    /**
     * 单位（g/条）
     */
    @ApiModelProperty(value="单位（g/条）")
    private String unit;
    /**
     * 换算量
     */
    @ApiModelProperty(value="换算量")
    private Integer conversion;
    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空")
    @ApiModelProperty(value="关键字")
    private String keywords;
    /**
     * 生产厂家ID
     */
    @ApiModelProperty(value="生产厂家ID")
    private String producterId;
    /**
     * 药品状态0正常0停用 sys_dict_data表 sys_normal_disable
     */
    @NotBlank(message = "药品状态不能为空")
    @ApiModelProperty(value="药品状态0正常0停用 sys_dict_data表 sys_normal_disable")
    private String status;
    /**
     * 库存量
     */
    @ApiModelProperty(value="库存量")
    private BigDecimal medicinesStockNum;
    /**
     * 预警值
     */
    @ApiModelProperty(value="预警值")
    private BigDecimal medicinesStockDangerNum;

}
