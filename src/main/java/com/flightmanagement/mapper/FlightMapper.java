package com.flightmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flightmanagement.entity.Flight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FlightMapper extends BaseMapper<Flight> {

    /**
     * 分页查询航班
     */
    IPage<Flight> selectPage(Page<Flight> page, @Param("flightNumber") String flightNumber,
                            @Param("departureAirport") String departureAirport,
                            @Param("arrivalAirport") String arrivalAirport,
                            @Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate);

    /**
     * 根据日期范围查询航班
     */
    List<Flight> selectByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    /**
     * 根据航线查询航班
     */
    List<Flight> selectByRoute(@Param("departureAirport") String departureAirport,
                              @Param("arrivalAirport") String arrivalAirport);
} 