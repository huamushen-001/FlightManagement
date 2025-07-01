package com.flightmanagement.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ExcelExportService {

    /**
     * 导出机组成员信息
     */
    void exportCrewMembers(HttpServletResponse response, List<Long> ids) throws IOException;

    /**
     * 导出航班信息
     */
    void exportFlights(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException;

    /**
     * 导出排班表
     */
    void exportSchedule(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException;

    /**
     * 导出统计报表
     */
    void exportStatistics(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException;
} 