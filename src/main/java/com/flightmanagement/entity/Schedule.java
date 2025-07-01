package com.flightmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("schedule")
public class Schedule {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "航班ID不能为空")
    @TableField("flight_id")
    private Long flightId;

    @NotNull(message = "机组成员ID不能为空")
    @TableField("crew_member_id")
    private Long crewMemberId;

    @TableField("role")
    private String role; // 机长、副驾驶、空乘等

    @TableField("status")
    private String status; // 已分配、已确认、已完成等

    @TableField("work_hours")
    private Integer workHours; // 工作时长（小时）

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 关联查询字段
    @TableField(exist = false)
    private Flight flight;

    @TableField(exist = false)
    private CrewMember crewMember;
} 