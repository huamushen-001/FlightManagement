package com.flightmanagement.common;

import lombok.Data;

@Data
public class PageRequest {
    private Integer current = 1;
    private Integer size = 10;
    private String keyword;
    private String startDate;
    private String endDate;
} 