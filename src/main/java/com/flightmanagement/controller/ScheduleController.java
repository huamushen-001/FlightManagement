package com.flightmanagement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flightmanagement.common.Result;
import com.flightmanagement.entity.Schedule;
import com.flightmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 分页查询排班计划
     */
    @GetMapping("/page")
    public Result<IPage<Schedule>> getPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) Long crewMemberId,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String crewMemberName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        IPage<Schedule> page = scheduleService.getPage(current, size, flightId, crewMemberId, flightNumber, crewMemberName, start, end);
        return Result.success(page);
    }

    /**
     * 根据机组成员ID查询排班计划
     */
    @GetMapping("/crew-member/{crewMemberId}")
    public Result<List<Schedule>> getByCrewMemberId(@PathVariable Long crewMemberId,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        List<Schedule> schedules = scheduleService.getByCrewMemberId(crewMemberId, start, end);
        return Result.success(schedules);
    }

    /**
     * 根据航班ID查询排班计划
     */
    @GetMapping("/flight/{flightId}")
    public Result<List<Schedule>> getByFlightId(@PathVariable Long flightId) {
        List<Schedule> schedules = scheduleService.getByFlightId(flightId);
        return Result.success(schedules);
    }

    /**
     * 检查时间冲突
     */
    @GetMapping("/check-conflict")
    public Result<Boolean> checkTimeConflict(@RequestParam Long crewMemberId,
                                           @RequestParam String startTime,
                                           @RequestParam String endTime,
                                           @RequestParam(required = false) Long excludeId) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        
        boolean hasConflict = scheduleService.checkTimeConflict(crewMemberId, start, end, excludeId);
        return Result.success(hasConflict);
    }

    /**
     * 统计机组成员工作时长
     */
    @GetMapping("/work-hours/{crewMemberId}")
    public Result<Integer> sumWorkHours(@PathVariable Long crewMemberId,
                                      @RequestParam(required = false) String startDate,
                                      @RequestParam(required = false) String endDate) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        Integer hours = scheduleService.sumWorkHours(crewMemberId, start, end);
        return Result.success(hours);
    }

    /**
     * 根据ID查询排班计划
     */
    @GetMapping("/{id}")
    public Result<Schedule> getById(@PathVariable Long id) {
        Schedule schedule = scheduleService.getById(id);
        return Result.success(schedule);
    }

    /**
     * 添加排班计划
     */
    @PostMapping
    public Result<Boolean> add(@Valid @RequestBody Schedule schedule) {
        boolean success = scheduleService.addSchedule(schedule);
        return success ? Result.success(true) : Result.error("添加失败");
    }

    /**
     * 更新排班计划
     */
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody Schedule schedule) {
        boolean success = scheduleService.updateSchedule(schedule);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 删除排班计划
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = scheduleService.deleteSchedule(id);
        return success ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 批量分配排班
     */
    @PostMapping("/batch")
    public Result<Boolean> batchAssign(@RequestBody List<Schedule> schedules) {
        boolean success = scheduleService.batchAssignSchedule(schedules);
        return success ? Result.success(true) : Result.error("批量分配失败");
    }
} 