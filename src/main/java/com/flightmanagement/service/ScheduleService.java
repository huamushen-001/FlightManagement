package com.flightmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flightmanagement.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService extends IService<Schedule> {

    /**
     * 分页查询排班计划
     */
    IPage<Schedule> getPage(Integer current, Integer size, Long flightId, Long crewMemberId, 
                           String flightNumber, String crewMemberName, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据机组成员ID查询排班计划
     */
    List<Schedule> getByCrewMemberId(Long crewMemberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据航班ID查询排班计划
     */
    List<Schedule> getByFlightId(Long flightId);

    /**
     * 添加排班计划
     */
    boolean addSchedule(Schedule schedule);

    /**
     * 更新排班计划
     */
    boolean updateSchedule(Schedule schedule);

    /**
     * 删除排班计划
     */
    boolean deleteSchedule(Long id);

    /**
     * 检查时间冲突
     */
    boolean checkTimeConflict(Long crewMemberId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 统计机组成员工作时长
     */
    Integer sumWorkHours(Long crewMemberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 批量分配排班
     */
    boolean batchAssignSchedule(List<Schedule> schedules);
} 