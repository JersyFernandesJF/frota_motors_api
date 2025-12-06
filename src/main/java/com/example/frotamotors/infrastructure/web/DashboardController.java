package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.service.DashboardService;
import com.example.frotamotors.infrastructure.dto.CategoryDistributionDTO;
import com.example.frotamotors.infrastructure.dto.ChartDataDTO;
import com.example.frotamotors.infrastructure.dto.DashboardStatsDTO;
import com.example.frotamotors.infrastructure.dto.PriceDistributionDTO;
import com.example.frotamotors.infrastructure.dto.RevenueTrendDTO;
import com.example.frotamotors.infrastructure.dto.TopBrandDTO;
import com.example.frotamotors.infrastructure.dto.UserActivityDTO;
import com.example.frotamotors.infrastructure.dto.UserGrowthDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

  @Autowired private DashboardService dashboardService;

  @GetMapping("/stats")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<DashboardStatsDTO> getStats() {
    DashboardStatsDTO stats = dashboardService.getStats();
    return ResponseEntity.ok(stats);
  }

  @GetMapping("/charts/listings-growth")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<ChartDataDTO>> getListingsGrowth(
      @RequestParam(required = false, defaultValue = "30d") String period) {
    List<ChartDataDTO> data = dashboardService.getListingsGrowth(period);
    return ResponseEntity.ok(data);
  }

  @GetMapping("/charts/category-distribution")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<CategoryDistributionDTO>> getCategoryDistribution() {
    List<CategoryDistributionDTO> distribution = dashboardService.getCategoryDistribution();
    return ResponseEntity.ok(distribution);
  }

  @GetMapping("/charts/top-brands")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<TopBrandDTO>> getTopBrands() {
    List<TopBrandDTO> topBrands = dashboardService.getTopBrands();
    return ResponseEntity.ok(topBrands);
  }

  @GetMapping("/charts/user-activity")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<UserActivityDTO>> getUserActivity(
      @RequestParam(required = false) String date) {
    List<UserActivityDTO> activity = dashboardService.getUserActivity(date);
    return ResponseEntity.ok(activity);
  }

  @GetMapping("/charts/price-distribution")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<PriceDistributionDTO>> getPriceDistribution() {
    List<PriceDistributionDTO> distribution = dashboardService.getPriceDistribution();
    return ResponseEntity.ok(distribution);
  }

  @GetMapping("/charts/user-growth")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<UserGrowthDTO>> getUserGrowth(
      @RequestParam(required = false, defaultValue = "30d") String period) {
    List<UserGrowthDTO> growth = dashboardService.getUserGrowth(period);
    return ResponseEntity.ok(growth);
  }

  @GetMapping("/charts/revenue-trend")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<RevenueTrendDTO>> getRevenueTrend(
      @RequestParam(required = false, defaultValue = "30d") String period) {
    List<RevenueTrendDTO> trend = dashboardService.getRevenueTrend(period);
    return ResponseEntity.ok(trend);
  }
}
