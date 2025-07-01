package com.flightmanagement.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flightmanagement.entity.Schedule;
import com.flightmanagement.entity.Flight;
import com.flightmanagement.mapper.ScheduleMapper;
import com.flightmanagement.mapper.FlightMapper;
import com.flightmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Autowired
    private FlightMapper flightMapper;

    @Override
    public IPage<Schedule> getPage(Integer current, Integer size, Long flightId, Long crewMemberId,
                                  String flightNumber, String crewMemberName, LocalDateTime startDate, LocalDateTime endDate) {
        Page<Schedule> page = new Page<>(current, size);
        return baseMapper.selectPageWithDetails(page, flightId, crewMemberId, flightNumber, crewMemberName, startDate, endDate);
    }

    @Override
    public List<Schedule> getByCrewMemberId(Long crewMemberId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.selectByCrewMemberId(crewMemberId, startDate, endDate);
    }

    @Override
    public List<Schedule> getByFlightId(Long flightId) {
        return baseMapper.selectByFlightId(flightId);
    }

    @Override
    @Transactional
    public boolean addSchedule(Schedule schedule) {
        // 根据flightId查询航班信息
        Flight flight = flightMapper.selectById(schedule.getFlightId());
        if (flight == null) {
            throw new RuntimeException("航班不存在");
        }
        
        // 检查时间冲突
        if (checkTimeConflict(schedule.getCrewMemberId(), 
                            flight.getDepartureTime(), 
                            flight.getArrivalTime(), null)) {
            throw new RuntimeException("该时间段已有排班安排，存在时间冲突");
        }
        
        schedule.setCreateTime(LocalDateTime.now());
        schedule.setUpdateTime(LocalDateTime.now());
        schedule.setStatus("已分配");
        
        return save(schedule);
    }

    @Override
    @Transactional
    public boolean updateSchedule(Schedule schedule) {
        // 根据flightId查询航班信息
        Flight flight = flightMapper.selectById(schedule.getFlightId());
        if (flight == null) {
            throw new RuntimeException("航班不存在");
        }
        
        // 检查时间冲突（排除当前记录）
        if (checkTimeConflict(schedule.getCrewMemberId(), 
                            flight.getDepartureTime(), 
                            flight.getArrivalTime(), schedule.getId())) {
            throw new RuntimeException("该时间段已有排班安排，存在时间冲突");
        }
        
        schedule.setUpdateTime(LocalDateTime.now());
        return updateById(schedule);
    }

    @Override
    public boolean deleteSchedule(Long id) {
        return removeById(id);
    }

    @Override
    public boolean checkTimeConflict(Long crewMemberId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        List<Schedule> conflicts = baseMapper.checkTimeConflict(crewMemberId, startTime, endTime, excludeId);
        return !conflicts.isEmpty();
    }

    @Override
    public Integer sumWorkHours(Long crewMemberId, LocalDateTime startDate, LocalDateTime endDate) {
        Integer hours = baseMapper.sumWorkHours(crewMemberId, startDate, endDate);
        return hours != null ? hours : 0;
    }

    @Override
    @Transactional
    public boolean batchAssignSchedule(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            if (!addSchedule(schedule)) {
                throw new RuntimeException("批量分配排班失败");
            }
        }
        return true;
    }
} 