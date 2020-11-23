package com.twofish.dto;

import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 供应商信息表Dto传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-dto-ProviderDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDto extends BaseDto {
    /**
     * 供应商id
     */
    @ApiModelProperty(value="供应商id")
    private Long providerId;
    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value="供应商名称")
    private String providerName;
    /**
     * 联系人名称
     */
    @NotBlank(message = "联系人名称不能为空")
    @ApiModelProperty(value="联系人名称")
    private String contactName;
    /**
     * 联系人手机
     */
    @NotBlank(message = "联系人手机不能为空")
    @ApiModelProperty(value="联系人手机")
    private String contactMobile;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value="联系人电话")
    private String contactTel;
    /**
     * 银行账号
     */
    @ApiModelProperty(value="银行账号")
    private String bankAccount;
    /**
     * 供应商地址
     */
    @ApiModelProperty(value="供应商地址")
    private String providerAddress;
    /**
     * 状态（0正常 1停用）sys_normal_disable
     */
    @NotBlank(message = "状态（0正常 1停用）sys_normal_disable不能为空")
    @ApiModelProperty(value="状态（0正常 1停用）sys_normal_disable")
    private String status;


}
