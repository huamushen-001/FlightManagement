package com.flightmanagement.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flightmanagement.entity.Flight;
import com.flightmanagement.mapper.FlightMapper;
import com.flightmanagement.service.FlightService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl extends ServiceImpl<FlightMapper, Flight> implements FlightService {

    @Override
    public IPage<Flight> getPage(Integer current, Integer size, String flightNumber, 
                                String departureAirport, String arrivalAirport,
                                LocalDateTime startDate, LocalDateTime endDate) {
        Page<Flight> page = new Page<>(current, size);
        return baseMapper.selectPage(page, flightNumber, departureAirport, arrivalAirport, startDate, endDate);
    }

    @Override
    public List<Flight> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.selectByDateRange(startDate, endDate);
    }

    @Override
    public List<Flight> getByRoute(String departureAirport, String arrivalAirport) {
        return baseMapper.selectByRoute(departureAirport, arrivalAirport);
    }

    @Override
    public boolean addFlight(Flight flight) {
        flight.setCreateTime(LocalDateTime.now());
        flight.setUpdateTime(LocalDateTime.now());
        flight.setStatus("计划");
        
        // 自动计算飞行时长
        if (flight.getFlightDuration() == null && flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            flight.setFlightDuration(calculateFlightDuration(flight.getDepartureTime(), flight.getArrivalTime()));
        }
        
        return save(flight);
    }

    @Override
    public boolean updateFlight(Flight flight) {
        flight.setUpdateTime(LocalDateTime.now());
        
        // 重新计算飞行时长
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            flight.setFlightDuration(calculateFlightDuration(flight.getDepartureTime(), flight.getArrivalTime()));
        }
        
        return updateById(flight);
    }

    @Override
    public boolean deleteFlight(Long id) {
        return removeById(id);
    }

    @Override
    public Integer calculateFlightDuration(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (departureTime != null && arrivalTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            return (int) duration.toMinutes();
        }
        return 0;
    }
} 