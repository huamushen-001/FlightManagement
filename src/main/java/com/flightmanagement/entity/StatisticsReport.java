package com.flightmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StatisticsReport {

    @ExcelProperty("统计项目")
    private String statisticItem;

    @ExcelProperty("数值")
    private String value;

    @ExcelProperty("备注")
    private String remark;

    public StatisticsReport() {}

    public StatisticsReport(String statisticItem, String value) {
        this.statisticItem = statisticItem;
        this.value = value;
    }

    public StatisticsReport(String statisticItem, String value, String remark) {
        this.statisticItem = statisticItem;
        this.value = value;
        this.remark = remark;
    }
} 