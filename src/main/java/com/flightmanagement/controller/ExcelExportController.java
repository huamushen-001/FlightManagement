package com.flightmanagement.controller;

import com.flightmanagement.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 导出机组成员信息
     */
    @GetMapping("/crew-members")
    public void exportCrewMembers(HttpServletResponse response,
                                 @RequestParam(required = false) List<Long> ids) throws IOException {
        excelExportService.exportCrewMembers(response, ids);
    }

    /**
     * 导出航班信息
     */
    @GetMapping("/flights")
    public void exportFlights(HttpServletResponse response,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) throws IOException {
        excelExportService.exportFlights(response, startDate, endDate);
    }

    /**
     * 导出排班表
     */
    @GetMapping("/schedule")
    public void exportSchedule(HttpServletResponse response,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) throws IOException {
        excelExportService.exportSchedule(response, startDate, endDate);
    }

    /**
     * 导出统计报表
     */
    @GetMapping("/statistics")
    public void exportStatistics(HttpServletResponse response,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) throws IOException {
        excelExportService.exportStatistics(response, startDate, endDate);
    }
} 