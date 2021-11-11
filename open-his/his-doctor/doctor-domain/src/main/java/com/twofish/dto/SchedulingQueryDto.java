package com.twofish.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/1/20 16:09
 * @description：排班查询条件传输类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "排班查询条件传输类")
public class SchedulingQueryDto implements Serializable {

    private Long deptId;
    private Long userId;
    //页面中上一周减一的日期 下一周加一天的日期 没有默认就是当天的日期
    private String queryDate;

    //要排班queryDate周的周一和周日的日期，需要通过queryDate计算出
    private String beginDate;
    private String endDate;

}
