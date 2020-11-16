package com.twofish.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 角色传输对象
 * @author ccy
 *
 */
@ApiModel(value="com-twofish-dto-RoleDto")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseDto {
    /**
     * 角色ID
     */
    @ApiModelProperty(value="角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value="角色名称")
    private String roleName;

    /**
     * 角色权限编码
     */
    @NotBlank(message = "角色权限编码不能为空")
    @ApiModelProperty(value="角色权限编码")
    private String roleCode;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value="显示顺序")
    private Integer roleSort;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotBlank(message = "角色状态不能为空")
    @ApiModelProperty(value="角色状态（0正常 1停用）")
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

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
