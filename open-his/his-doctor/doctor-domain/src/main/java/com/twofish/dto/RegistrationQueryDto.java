package com.twofish.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/3/9 21:43
 * @description：挂号时有号科室的查询条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "挂号时有号科室的查询条件对象")
public class RegistrationQueryDto implements Serializable{
    private Long deptId;
    @NotBlank(message = "挂号类型不能为空")
    private String schedulingType;
    @NotBlank(message = "挂号时段不能为空")
    private String subsectionType;
    @NotBlank(message = "挂号时间不能为空")
    private String schedulingDay;
}
