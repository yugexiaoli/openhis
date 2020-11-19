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
 * 挂号费用Dto传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-dto-RegisteredItemDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredItemDto extends BaseDto {
    /**
     * 挂号项ID
     */
    @ApiModelProperty(value="挂号项ID")
    private Long regItemId;
    /**
     * 挂号项目名称
     */
    @NotBlank(message = "挂号项目名称不能为空")
    @ApiModelProperty(value="挂号项目名称")
    private String regItemName;
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty(value="金额")
    private BigDecimal regItemFee;
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
    /**
     * 状态（0正常 1停用）
     */
    @NotBlank(message = "状态（0正常 1停用）不能为空")
    @ApiModelProperty(value="状态（0正常 1停用）")
    private String status;
    /**
     * 删除标志（0正常 1删除）
     */
    @ApiModelProperty(value="删除标志（0正常 1删除）")
    private String delFlag;

}
