package com.flightmanagement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flightmanagement.common.Result;
import com.flightmanagement.entity.Flight;
import com.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin
public class FlightController {

    @Autowired
    private FlightService flightService;

    /**
     * 分页查询航班
     */
    @GetMapping("/page")
    public Result<IPage<Flight>> getPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String departureAirport,
            @RequestParam(required = false) String arrivalAirport,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        
        IPage<Flight> page = flightService.getPage(current, size, flightNumber, departureAirport, arrivalAirport, startDate, endDate);
        return Result.success(page);
    }

    /**
     * 根据日期范围查询航班
     */
    @GetMapping("/date-range")
    public Result<List<Flight>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        
        List<Flight> flights = flightService.getByDateRange(startDate, endDate);
        return Result.success(flights);
    }

    /**
     * 根据航线查询航班
     */
    @GetMapping("/route")
    public Result<List<Flight>> getByRoute(
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport) {
        
        List<Flight> flights = flightService.getByRoute(departureAirport, arrivalAirport);
        return Result.success(flights);
    }

    /**
     * 根据ID查询航班
     */
    @GetMapping("/{id}")
    public Result<Flight> getById(@PathVariable Long id) {
        Flight flight = flightService.getById(id);
        return Result.success(flight);
    }

    /**
     * 添加航班
     */
    @PostMapping
    public Result<Boolean> add(@Valid @RequestBody Flight flight) {
        boolean success = flightService.addFlight(flight);
        return success ? Result.success(true) : Result.error("添加失败");
    }

    /**
     * 更新航班信息
     */
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody Flight flight) {
        boolean success = flightService.updateFlight(flight);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 删除航班
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = flightService.deleteFlight(id);
        return success ? Result.success(true) : Result.error("删除失败");
    }
} 