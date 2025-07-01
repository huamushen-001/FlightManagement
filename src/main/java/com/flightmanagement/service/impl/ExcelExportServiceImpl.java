package com.flightmanagement.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.flightmanagement.entity.CrewMember;
import com.flightmanagement.entity.Flight;
import com.flightmanagement.entity.Schedule;
import com.flightmanagement.entity.StatisticsReport;
import com.flightmanagement.service.CrewMemberService;
import com.flightmanagement.service.ExcelExportService;
import com.flightmanagement.service.FlightService;
import com.flightmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private CrewMemberService crewMemberService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void exportCrewMembers(HttpServletResponse response, List<Long> ids) throws IOException {
        List<CrewMember> crewMembers;
        if (ids != null && !ids.isEmpty()) {
            crewMembers = crewMemberService.listByIds(ids);
        } else {
            crewMembers = crewMemberService.list();
        }

        String fileName = URLEncoder.encode("机组成员信息", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), CrewMember.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("机组成员信息")
                .doWrite(crewMembers);
    }

    @Override
    public void exportFlights(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Flight> flights = flightService.getByDateRange(startDate, endDate);

        String fileName = URLEncoder.encode("航班信息", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), Flight.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("航班信息")
                .doWrite(flights);
    }

    @Override
    public void exportSchedule(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Schedule> schedules = scheduleService.getByCrewMemberId(null, startDate, endDate);

        String fileName = URLEncoder.encode("排班表", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), Schedule.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("排班表")
                .doWrite(schedules);
    }

    @Override
    public void exportStatistics(HttpServletResponse response, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        System.out.println("开始导出统计报表...");
        System.out.println("开始日期: " + startDate);
        System.out.println("结束日期: " + endDate);
        
        // 获取统计数据
        List<StatisticsReport> statisticsData = generateStatisticsReportData(startDate, endDate);
        
        System.out.println("生成的统计数据条数: " + statisticsData.size());
        
        String fileName = URLEncoder.encode("综合统计报表", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 使用StatisticsReport实体类导出
        EasyExcel.write(response.getOutputStream(), StatisticsReport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("综合统计")
                .doWrite(statisticsData);
        
        System.out.println("统计报表导出完成");
    }

    /**
     * 生成统计报表数据
     */
    private List<StatisticsReport> generateStatisticsReportData(LocalDateTime startDate, LocalDateTime endDate) {
        List<StatisticsReport> data = new ArrayList<>();
        
        try {
            // 1. 总体统计
            data.add(new StatisticsReport("总体统计", "", ""));
            data.add(new StatisticsReport("机组成员总数", String.valueOf(crewMemberService.count())));
            data.add(new StatisticsReport("航班总数", String.valueOf(flightService.count())));
            data.add(new StatisticsReport("排班记录总数", String.valueOf(scheduleService.count())));
            data.add(new StatisticsReport("", "", "")); // 空行
            
            System.out.println("总体统计: 机组成员=" + crewMemberService.count() + 
                             ", 航班=" + flightService.count() + 
                             ", 排班=" + scheduleService.count());
            
            // 2. 按职位统计机组成员
            List<CrewMember> allCrewMembers = crewMemberService.list();
            System.out.println("机组成员总数: " + allCrewMembers.size());
            
            data.add(new StatisticsReport("职位分布统计", "", ""));
            Map<String, Long> positionStats = allCrewMembers.stream()
                    .collect(Collectors.groupingBy(CrewMember::getPosition, Collectors.counting()));
            
            for (Map.Entry<String, Long> entry : positionStats.entrySet()) {
                data.add(new StatisticsReport(entry.getKey(), String.valueOf(entry.getValue())));
            }
            data.add(new StatisticsReport("", "", "")); // 空行
            
            // 3. 按资质统计机组成员
            data.add(new StatisticsReport("资质分布统计", "", ""));
            Map<String, Long> qualificationStats = allCrewMembers.stream()
                    .collect(Collectors.groupingBy(CrewMember::getQualificationLevel, Collectors.counting()));
            
            for (Map.Entry<String, Long> entry : qualificationStats.entrySet()) {
                data.add(new StatisticsReport(entry.getKey(), String.valueOf(entry.getValue())));
            }
            data.add(new StatisticsReport("", "", "")); // 空行
            
            // 4. 时间范围内的统计
            if (startDate != null && endDate != null) {
                List<Flight> flightsInRange = flightService.getByDateRange(startDate, endDate);
                List<Schedule> schedulesInRange = scheduleService.getByCrewMemberId(null, startDate, endDate);
                
                System.out.println("时间范围内航班数: " + flightsInRange.size());
                System.out.println("时间范围内排班数: " + schedulesInRange.size());
                
                data.add(new StatisticsReport("时间范围统计", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 至 " + endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                data.add(new StatisticsReport("航班数量", String.valueOf(flightsInRange.size())));
                data.add(new StatisticsReport("排班记录数量", String.valueOf(schedulesInRange.size())));
                data.add(new StatisticsReport("", "", "")); // 空行
                
                // 5. 按航线统计航班
                if (!flightsInRange.isEmpty()) {
                    data.add(new StatisticsReport("航线统计", "", ""));
                    Map<String, Long> routeStats = flightsInRange.stream()
                            .map(flight -> flight.getDepartureAirport() + " → " + flight.getArrivalAirport())
                            .collect(Collectors.groupingBy(route -> route, Collectors.counting()));
                    
                    for (Map.Entry<String, Long> entry : routeStats.entrySet()) {
                        data.add(new StatisticsReport(entry.getKey(), String.valueOf(entry.getValue()) + "次"));
                    }
                    data.add(new StatisticsReport("", "", "")); // 空行
                }
                
                // 6. 按机组成员统计工作时长
                if (!schedulesInRange.isEmpty()) {
                    data.add(new StatisticsReport("工作时长统计", "", ""));
                    Map<Long, Double> workHoursStats = new HashMap<>();
                    for (Schedule schedule : schedulesInRange) {
                        Long crewMemberId = schedule.getCrewMemberId();
                        double hours = workHoursStats.getOrDefault(crewMemberId, 0.0);
                        // 计算工作时长（这里简化处理，实际应该根据开始和结束时间计算）
                        hours += 8.0; // 假设每次排班8小时
                        workHoursStats.put(crewMemberId, hours);
                    }
                    
                    for (Map.Entry<Long, Double> entry : workHoursStats.entrySet()) {
                        CrewMember crewMember = crewMemberService.getById(entry.getKey());
                        if (crewMember != null) {
                            data.add(new StatisticsReport(crewMember.getName(), String.valueOf(entry.getValue()) + "小时"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("生成统计数据时发生错误: " + e.getMessage());
            e.printStackTrace();
            
            // 添加错误信息到数据中
            data.add(new StatisticsReport("错误信息", e.getMessage()));
        }
        
        return data;
    }
} 