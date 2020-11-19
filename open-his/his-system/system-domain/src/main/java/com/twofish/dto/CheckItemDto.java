package com.twofish.dto;

import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 检查费用表Dto传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-dto-CheckItemDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CheckItemDto extends BaseDto {

    /**
     * 项目费用ID
     */
    @ApiModelProperty(value="项目费用ID")
    private Long checkItemId;
    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @ApiModelProperty(value="项目名称")
    private String checkItemName;
    /**
     * 关键字【查询用】
     */
    @ApiModelProperty(value="关键字【查询用】")
    private String keywords;
    /**
     * 项目单价
     */
    @NotNull(message = "项目单价不能为空")
    @ApiModelProperty(value="项目单价")
    private BigDecimal unitPrice;
    /**
     * 项目成本
     */
    @NotNull(message = "项目成本不能为空")
    @ApiModelProperty(value="项目成本")
    private BigDecimal cost;
    /**
     * 单位
     */
    @ApiModelProperty(value="单位")
    private String unit;
    /**
     * 项目类别IDsxt_sys_dict_type
     */
    @NotBlank(message = "项目类别不能为空")
    @ApiModelProperty(value="项目类别IDsxt_sys_dict_type")
    private String typeId;
    /**
     * 状态0正常1停用 sxt_sys_dict_type
     */
    @NotBlank(message = "状态0正常1停用 sxt_sys_dict_type不能为空")
    @ApiModelProperty(value="状态0正常1停用 sxt_sys_dict_type")
    private String status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 创建者
     */
    @ApiModelProperty(value="创建者")
    private String createBy;
    /**
     * 更新者
     */
    @ApiModelProperty(value="更新者")
    private String updateBy;

}
