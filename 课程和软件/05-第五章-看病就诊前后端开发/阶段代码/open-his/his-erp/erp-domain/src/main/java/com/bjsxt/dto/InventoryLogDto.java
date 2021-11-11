package com.bjsxt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bjsxt.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 尚学堂 雷哥
 */

@ApiModel(value = "com-bjsxt-dto-InventoryLogDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogDto extends BaseDto {

    /**
     * 采购单据ID
     */
    @ApiModelProperty(value = "采购单据ID")
    private String purchaseId;

    /**
     * 药品名称
     */
    @ApiModelProperty(value = "药品名称")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @ApiModelProperty(value = "药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @ApiModelProperty(value = "处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;

    /**
     * 生产厂家ID
     */
    @ApiModelProperty(value = "生产厂家ID")
    private String producterId;
}