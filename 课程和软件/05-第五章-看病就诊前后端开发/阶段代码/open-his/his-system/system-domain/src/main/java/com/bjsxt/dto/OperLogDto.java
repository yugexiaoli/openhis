package com.bjsxt.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: 尚学堂 雷哥
 */
@ApiModel(value = "com-bjsxt-domain-OperLog")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_oper_log")
public class OperLogDto extends BaseDto {
    /**
     * 模块标题
     */
    @ApiModelProperty(value = "模块标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
    private String businessType;

    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员")
    private String operName;

    /**
     * 操作状态（0正常 1异常）
     */
    @ApiModelProperty(value = "操作状态（0成功 1失败）")
    private String status;
}
