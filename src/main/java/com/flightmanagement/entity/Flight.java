package com.flightmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("flight")
public class Flight {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "航班号不能为空")
    @TableField("flight_number")
    private String flightNumber;

    @NotBlank(message = "出发机场不能为空")
    @TableField("departure_airport")
    private String departureAirport;

    @NotBlank(message = "到达机场不能为空")
    @TableField("arrival_airport")
    private String arrivalAirport;

    @NotNull(message = "出发时间不能为空")
    @TableField("departure_time")
    private LocalDateTime departureTime;

    @NotNull(message = "到达时间不能为空")
    @TableField("arrival_time")
    private LocalDateTime arrivalTime;

    @TableField("flight_duration")
    private Integer flightDuration; // 飞行时长（分钟）

    @TableField("aircraft_type")
    private String aircraftType; // 机型

    @TableField("status")
    private String status; // 计划、执行中、已完成、取消等

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 