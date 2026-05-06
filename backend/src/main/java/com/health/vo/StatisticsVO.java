package com.health.vo;

import lombok.Data;

/**
 * 统计数据 VO
 */
@Data
public class StatisticsVO {
    
    private Long totalUsers;
    private Long totalArticles;
    private Long totalAppointments;
    private Long todayAppointments;
    private Long activeUsers;
    private Long newUsersThisMonth;
    
    public StatisticsVO() {
        this.totalUsers = 0L;
        this.totalArticles = 0L;
        this.totalAppointments = 0L;
        this.todayAppointments = 0L;
        this.activeUsers = 0L;
        this.newUsersThisMonth = 0L;
    }
}
