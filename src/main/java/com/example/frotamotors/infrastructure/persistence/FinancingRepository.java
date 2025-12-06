package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.FinancingStatus;
import com.example.frotamotors.domain.model.Financing;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancingRepository extends JpaRepository<Financing, UUID> {
  List<Financing> findByBuyerId(UUID buyerId);

  List<Financing> findBySellerId(UUID sellerId);

  List<Financing> findByVehicleId(UUID vehicleId);

  List<Financing> findByStatus(FinancingStatus status);

  @Query(
      "SELECT f FROM Financing f WHERE "
          + "(:buyerId IS NULL OR f.buyer.id = :buyerId) AND "
          + "(:sellerId IS NULL OR f.seller.id = :sellerId) AND "
          + "(:vehicleId IS NULL OR f.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR f.status = :status)")
  List<Financing> search(
      @Param("buyerId") UUID buyerId,
      @Param("sellerId") UUID sellerId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") FinancingStatus status);

  @Query(
      "SELECT f FROM Financing f WHERE "
          + "(:buyerId IS NULL OR f.buyer.id = :buyerId) AND "
          + "(:sellerId IS NULL OR f.seller.id = :sellerId) AND "
          + "(:vehicleId IS NULL OR f.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR f.status = :status) AND "
          + "(:minAmount IS NULL OR f.financingAmount >= :minAmount) AND "
          + "(:maxAmount IS NULL OR f.financingAmount <= :maxAmount) AND "
          + "(:startDate IS NULL OR f.createdAt >= :startDate) AND "
          + "(:endDate IS NULL OR f.createdAt <= :endDate)")
  Page<Financing> searchPageable(
      @Param("buyerId") UUID buyerId,
      @Param("sellerId") UUID sellerId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") FinancingStatus status,
      @Param("minAmount") java.math.BigDecimal minAmount,
      @Param("maxAmount") java.math.BigDecimal maxAmount,
      @Param("startDate") java.time.LocalDateTime startDate,
      @Param("endDate") java.time.LocalDateTime endDate,
      Pageable pageable);

  Page<Financing> findByBuyerId(UUID buyerId, Pageable pageable);

  Page<Financing> findBySellerId(UUID sellerId, Pageable pageable);

  Page<Financing> findByVehicleId(UUID vehicleId, Pageable pageable);

  Page<Financing> findByStatus(FinancingStatus status, Pageable pageable);
}
