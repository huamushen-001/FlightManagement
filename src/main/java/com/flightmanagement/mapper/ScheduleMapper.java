package com.flightmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flightmanagement.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    /**
     * 分页查询排班计划（包含关联信息）
     */
    IPage<Schedule> selectPageWithDetails(Page<Schedule> page, @Param("flightId") Long flightId,
                                         @Param("crewMemberId") Long crewMemberId,
                                         @Param("flightNumber") String flightNumber,
                                         @Param("crewMemberName") String crewMemberName,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * 根据机组成员ID查询排班计划
     */
    List<Schedule> selectByCrewMemberId(@Param("crewMemberId") Long crewMemberId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 根据航班ID查询排班计划
     */
    List<Schedule> selectByFlightId(@Param("flightId") Long flightId);

    /**
     * 检查时间冲突
     */
    List<Schedule> checkTimeConflict(@Param("crewMemberId") Long crewMemberId,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime,
                                   @Param("excludeId") Long excludeId);

    /**
     * 统计机组成员工作时长
     */
    Integer sumWorkHours(@Param("crewMemberId") Long crewMemberId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
} 