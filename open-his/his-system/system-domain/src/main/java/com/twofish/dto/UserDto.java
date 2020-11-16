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
 * 用户传输对象
 * @author ccy
 *
 */
@ApiModel(value="com-twofish-dto-UserDto")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {
    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    @ApiModelProperty(value="部门ID")
    private Long deptId;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @ApiModelProperty(value="用户账号")
    private String userName;

    /**
     * 用户类型（0超级用户为 1为系统用户）
     */
    @ApiModelProperty(value="用户类型（0超级用户为 1为系统用户）")
    private String userType;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @NotBlank(message = "用户性别不能为空")
    @ApiModelProperty(value="用户性别（0男 1女 2未知）")
    private String sex;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value="年龄")
    private Integer age;

    /**
     * 头像
     */
    @ApiModelProperty(value="头像")
    private String picture;

    /**
     * 学历 sys_dict_type:sys_user_background
     */
    @ApiModelProperty(value="学历 sys_dict_type:sys_user_background")
    private String background;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空")
    @ApiModelProperty(value="电话")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value="用户邮箱")
    private String email;

    /**
     * 擅长
     */
    @ApiModelProperty(value="擅长")
    private String strong;

    /**
     * 荣誉
     */
    @ApiModelProperty(value="荣誉")
    private String honor;

    /**
     * 简介
     */
    @ApiModelProperty(value="简介")
    private String introduction;

    /**
     *
     医生级别sys_dict_type:sys_user_level
     */
    @NotBlank(message = "医生级别不能为空")
    @ApiModelProperty(value=",医生级别sys_dict_type:sys_user_level")
    private String userRank;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 最后一次登录时间
     */
    @ApiModelProperty(value="最后一次登录时间")
    private Date lastLoginTime;

    /**
     * 最后登陆IP
     */
    @ApiModelProperty(value="最后登陆IP")
    private String lastLoginIp;

    /**
     * 帐号状态（0正常 1停用）
     */
    @NotBlank(message = "帐号状态不可为空")
    @ApiModelProperty(value="帐号状态（0正常 1停用）")
    private String status;


    @ApiModelProperty(value="")
    private String unionId;

    /**
     * 用户授权登录openid 扩展第三方登陆使用
     */
    @ApiModelProperty(value="用户授权登录openid 扩展第三方登陆使用")
    private String openId;

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
     * 盐
     */
    @ApiModelProperty(value="盐")
    private String salt;

    /**
     * 删除标志（0正常 1删除）
     */
    @TableLogic
    @ApiModelProperty(value="删除标志（0正常 1删除）")
    private String delFlag;

    /**
     * 是否需要参与排班0需要,1 不需要
     */
    @ApiModelProperty(value="是否需要参与排班0需要,1 不需要")
    private String schedulingFlag;

}
