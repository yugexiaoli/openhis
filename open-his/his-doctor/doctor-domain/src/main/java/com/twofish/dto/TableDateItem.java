package com.twofish.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

/**
 * @author ：ChenChangYu
 * @date ：Created in 2021/1/20 17:10
 * @description：用于组装表格数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDateItem implements Serializable {
    private Long userId;
    private Long deptId;
    private String subsectionType;
    private Collection<String> schedulingType;


    //用于填充具体排班 key为日期，value为排班类型: 1-门诊 2-急诊 不填-没有 ,最后要将value封装到schedulingType里
    @JsonIgnore
    private TreeMap<String,String> records;

    public TableDateItem(Long userId, Long deptId, String subsectionType, TreeMap<String, String> records) {
        this.userId = userId;
        this.deptId = deptId;
        this.subsectionType = subsectionType;
        this.records = records;
    }
}
