package com.twofish.dto;

import com.twofish.domain.SimpleUser;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/1/21 11:59
 * @description：前端修改排班表单接收对象
 */
@ApiModel(value = "前端修改排班表单接收对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingFormDto implements Serializable {
    private List<TableDateItem> data;
    private String startDate;
    private SimpleUser simpleUser;
}
