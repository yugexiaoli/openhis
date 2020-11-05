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

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 操作日志传输对象
 * @author ccy
 */
@ApiModel(value="com-twofish-dto-OperLogDto")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class OperLogDto extends BaseDto {
    /**
     * 模块标题
     */
    @NotNull(message = "模块标题不能为空")
    @ApiModelProperty(value="模块标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @NotNull(message = "业务类型不能为空")
    @ApiModelProperty(value="业务类型（0其它 1新增 2修改 3删除）")
    private String businessType;

    /**
     * 操作人员
     */
    @ApiModelProperty(value="操作人员")
    private String operName;

    /**
     * 操作状态（0正常 1异常）
     */
    @ApiModelProperty(value="操作状态（0正常 1异常）")
    private String status;

}
