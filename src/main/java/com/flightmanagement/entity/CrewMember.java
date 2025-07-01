package com.flightmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crew_member")
public class CrewMember {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @TableField("name")
    private String name;

    @NotBlank(message = "工号不能为空")
    @TableField("employee_id")
    private String employeeId;

    @NotBlank(message = "岗位不能为空")
    @TableField("position")
    private String position; // 飞行员、空乘、维修等

    @NotBlank(message = "资质等级不能为空")
    @TableField("qualification_level")
    private String qualificationLevel; // 高级、中级、初级等

    @NotBlank(message = "联系方式不能为空")
    @TableField("contact")
    private String contact;

    @TableField("email")
    private String email;

    @TableField("status")
    private String status; // 在职、休假、离职等

    @TableField("hire_date")
    private LocalDateTime hireDate;

    @TableField("max_flight_hours")
    private Integer maxFlightHours; // 最大飞行小时数

    @TableField("current_flight_hours")
    private Integer currentFlightHours; // 当前飞行小时数

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic//逻辑删除，0表示未删除，1表示已删除，防误删
    @TableField("deleted")
    private Integer deleted;
} 