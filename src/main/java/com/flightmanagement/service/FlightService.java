package com.flightmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flightmanagement.entity.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService extends IService<Flight> {

    /**
     * 分页查询航班
     */
    IPage<Flight> getPage(Integer current, Integer size, String flightNumber, 
                         String departureAirport, String arrivalAirport,
                         LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据日期范围查询航班
     */
    List<Flight> getByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据航线查询航班
     */
    List<Flight> getByRoute(String departureAirport, String arrivalAirport);

    /**
     * 添加航班
     */
    boolean addFlight(Flight flight);

    /**
     * 更新航班信息
     */
    boolean updateFlight(Flight flight);

    /**
     * 删除航班
     */
    boolean deleteFlight(Long id);

    /**
     * 计算飞行时长
     */
    Integer calculateFlightDuration(LocalDateTime departureTime, LocalDateTime arrivalTime);
} 