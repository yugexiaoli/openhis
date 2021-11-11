package com.bjsxt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @Author: 尚学堂 雷哥
*/

/**
    * 生产厂家表
    */
@ApiModel(value="com-bjsxt-domain-Producter")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stock_producter")
public class Producter extends BaseEntity {
    /**
     * 厂家ID
     */
    @TableId(value = "producter_id", type = IdType.AUTO)
    @ApiModelProperty(value="厂家ID")
    private Long producterId;

    /**
     * 厂家名称
     */
    @TableField(value = "producter_name")
    @ApiModelProperty(value="厂家名称")
    private String producterName;

    /**
     * 厂家简码 搜索用
     */
    @TableField(value = "producter_code")
    @ApiModelProperty(value="厂家简码 搜索用")
    private String producterCode;

    /**
     * 厂家地址 
     */
    @TableField(value = "producter_address")
    @ApiModelProperty(value="厂家地址 ")
    private String producterAddress;

    /**
     * 厂家电话
     */
    @TableField(value = "producter_tel")
    @ApiModelProperty(value="厂家电话")
    private String producterTel;

    /**
     * 联系人
     */
    @TableField(value = "producter_person")
    @ApiModelProperty(value="联系人")
    private String producterPerson;

    /**
     * 关键字
     */
    @TableField(value = "keywords")
    @ApiModelProperty(value="关键字")
    private String keywords;

    /**
     * 状态标志（0正常 1停用）sys_normal_disable
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态标志（0正常 1停用）sys_normal_disable")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新者")
    private String updateBy;

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_PRODUCTER_NAME = "producter_name";

    public static final String COL_PRODUCTER_CODE = "producter_code";

    public static final String COL_PRODUCTER_ADDRESS = "producter_address";

    public static final String COL_PRODUCTER_TEL = "producter_tel";

    public static final String COL_PRODUCTER_PERSON = "producter_person";

    public static final String COL_KEYWORDS = "keywords";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}