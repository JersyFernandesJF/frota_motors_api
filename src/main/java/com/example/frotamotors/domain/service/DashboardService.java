package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ListingModerationStatus;
import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.infrastructure.dto.CategoryDistributionDTO;
import com.example.frotamotors.infrastructure.dto.ChartDataDTO;
import com.example.frotamotors.infrastructure.dto.DashboardStatsDTO;
import com.example.frotamotors.infrastructure.dto.TopBrandDTO;
import com.example.frotamotors.infrastructure.dto.TrendDTO;
import com.example.frotamotors.infrastructure.dto.UserActivityDTO;
import com.example.frotamotors.infrastructure.persistence.ComplaintRepository;
import com.example.frotamotors.infrastructure.persistence.MessageRepository;
import com.example.frotamotors.infrastructure.persistence.PaymentRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private ComplaintRepository complaintRepository;

  @Autowired private MessageRepository messageRepository;

  @Autowired private PaymentRepository paymentRepository;

  public DashboardStatsDTO getStats() {
    Long totalUsers = userRepository.count();
    Long totalListings = vehicleRepository.count();

    Long pendingApprovals =
        vehicleRepository.countByModerationStatus(ListingModerationStatus.PENDING);

    Long pendingReports = complaintRepository.countByStatus(ComplaintStatus.PENDING);

    Long totalMessages = messageRepository.count();

    // New users this month
    LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
    Long newUsersThisMonth = userRepository.countByCreatedAtAfter(startOfMonth.atStartOfDay());

    // Conversion rate (completed payments / total payments)
    Long totalPayments = paymentRepository.count();
    Long completedPayments = paymentRepository.countByStatus(PaymentStatus.COMPLETED);
    Double conversionRate = 0.0;
    if (totalPayments > 0) {
      conversionRate = (completedPayments.doubleValue() / totalPayments.doubleValue()) * 100.0;
      conversionRate =
          BigDecimal.valueOf(conversionRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Total revenue (sum of completed payments)
    BigDecimal totalRevenue = paymentRepository.sumAmountByStatus(PaymentStatus.COMPLETED);
    if (totalRevenue == null) {
      totalRevenue = BigDecimal.ZERO;
    }

    // Trends (simplified - comparing this month vs last month)
    LocalDate startOfLastMonth = startOfMonth.minusMonths(1);
    LocalDate endOfLastMonth = startOfMonth.minusDays(1);
    Long usersThisMonth = newUsersThisMonth;
    Long usersLastMonth =
        userRepository.countByCreatedAtBetween(
            startOfLastMonth.atStartOfDay(), endOfLastMonth.atTime(23, 59, 59));

    Double usersTrendValue = 0.0;
    Boolean usersTrendPositive = true;
    if (usersLastMonth > 0) {
      usersTrendValue =
          ((usersThisMonth.doubleValue() - usersLastMonth.doubleValue())
                  / usersLastMonth.doubleValue())
              * 100.0;
      usersTrendPositive = usersTrendValue >= 0;
      usersTrendValue = Math.abs(usersTrendValue);
      usersTrendValue =
          BigDecimal.valueOf(usersTrendValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    Long listingsThisMonth = vehicleRepository.countByCreatedAtAfter(startOfMonth.atStartOfDay());
    Long listingsLastMonth =
        vehicleRepository.countByCreatedAtBetween(
            startOfLastMonth.atStartOfDay(), endOfLastMonth.atTime(23, 59, 59));

    Double listingsTrendValue = 0.0;
    Boolean listingsTrendPositive = true;
    if (listingsLastMonth > 0) {
      listingsTrendValue =
          ((listingsThisMonth.doubleValue() - listingsLastMonth.doubleValue())
                  / listingsLastMonth.doubleValue())
              * 100.0;
      listingsTrendPositive = listingsTrendValue >= 0;
      listingsTrendValue = Math.abs(listingsTrendValue);
      listingsTrendValue =
          BigDecimal.valueOf(listingsTrendValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    return new DashboardStatsDTO(
        totalUsers,
        totalListings,
        pendingApprovals,
        pendingReports,
        totalMessages,
        newUsersThisMonth,
        conversionRate,
        totalRevenue,
        new TrendDTO(usersTrendValue, usersTrendPositive),
        new TrendDTO(listingsTrendValue, listingsTrendPositive));
  }

  public List<ChartDataDTO> getListingsGrowth(String period) {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate;

    switch (period) {
      case "7d":
        startDate = endDate.minusDays(7);
        break;
      case "30d":
        startDate = endDate.minusDays(30);
        break;
      case "90d":
        startDate = endDate.minusDays(90);
        break;
      case "1y":
        startDate = endDate.minusYears(1);
        break;
      default:
        startDate = endDate.minusDays(30);
    }

    List<ChartDataDTO> data = new ArrayList<>();
    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {
      LocalDateTime startOfDay = currentDate.atStartOfDay();
      LocalDateTime endOfDay = currentDate.atTime(23, 59, 59);

      Long count = vehicleRepository.countByCreatedAtBetween(startOfDay, endOfDay);
      data.add(new ChartDataDTO(currentDate.format(DateTimeFormatter.ISO_DATE), count));

      currentDate = currentDate.plusDays(1);
    }

    return data;
  }

  public List<CategoryDistributionDTO> getCategoryDistribution() {
    // Get distribution by vehicle type
    List<Object[]> results = vehicleRepository.countByType();
    List<CategoryDistributionDTO> distribution = new ArrayList<>();

    String[] colors = {"#3b82f6", "#8b5cf6", "#ec4899", "#f59e0b", "#10b981", "#ef4444"};
    int colorIndex = 0;

    for (Object[] result : results) {
      com.example.frotamotors.domain.enums.VehicleType type =
          (com.example.frotamotors.domain.enums.VehicleType) result[0];
      Long count = ((Number) result[1]).longValue();
      distribution.add(
          new CategoryDistributionDTO(type.name(), count, colors[colorIndex % colors.length]));
      colorIndex++;
    }

    return distribution;
  }

  public List<TopBrandDTO> getTopBrands() {
    List<Object[]> results = vehicleRepository.findTopBrands();
    return results.stream()
        .map(result -> new TopBrandDTO((String) result[0], ((Number) result[1]).longValue()))
        .collect(Collectors.toList());
  }

  public List<UserActivityDTO> getUserActivity(String date) {
    LocalDate targetDate;
    if (date != null && !date.isEmpty()) {
      targetDate = LocalDate.parse(date);
    } else {
      targetDate = LocalDate.now();
    }

    List<UserActivityDTO> activity = new ArrayList<>();

    for (int hour = 0; hour < 24; hour++) {
      LocalDateTime startOfHour = targetDate.atTime(hour, 0);
      LocalDateTime endOfHour = targetDate.atTime(hour, 59, 59);

      Long count = userRepository.countByCreatedAtBetween(startOfHour, endOfHour);
      activity.add(new UserActivityDTO(String.format("%02d:00", hour), count));
    }

    return activity;
  }
}
