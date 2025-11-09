package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;

public record DashboardStatsDTO(
    Long totalUsers,
    Long totalListings,
    Long pendingApprovals,
    Long pendingReports,
    Long totalMessages,
    Long newUsersThisMonth,
    Double conversionRate,
    BigDecimal totalRevenue,
    TrendDTO usersTrend,
    TrendDTO listingsTrend) {}

