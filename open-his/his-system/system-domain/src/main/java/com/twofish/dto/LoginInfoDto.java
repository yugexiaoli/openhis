package com.twofish.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.twofish.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 登录日志数据传输对象
 * @author ccy
 *
 */
@ApiModel(value="com-twofish-dto-LoginInfoDto")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDto extends BaseDto {
    /**
     * 访问ID
     */
    @ApiModelProperty(value="访问ID")
    private Long infoId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户名称")
    private String userName;

    /**
     * 登陆账号
     */
    @ApiModelProperty(value="登陆账号")
    private String loginAccount;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value="登录IP地址")
    private String ipAddr;

    /**
     * 登录地点
     */
    @ApiModelProperty(value="登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ApiModelProperty(value="浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty(value="操作系统")
    private String os;

    /**
     * 登录状态（0成功 1失败）字典表
     */
    @ApiModelProperty(value="登录状态（0成功 1失败）字典表")
    private String loginStatus;

    /**
     * 登陆类型0系统用户1患者用户 字典表
     */
    @ApiModelProperty(value="登陆类型0系统用户1患者用户 字典表")
    private String loginType;

    /**
     * 提示消息
     */
    @ApiModelProperty(value="提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="访问时间")
    private Date loginTime;

}
