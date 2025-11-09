package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.InspectionStatus;
import com.example.frotamotors.domain.model.Inspection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, UUID> {
  List<Inspection> findByBuyerId(UUID buyerId);

  List<Inspection> findBySellerId(UUID sellerId);

  List<Inspection> findByInspectorId(UUID inspectorId);

  List<Inspection> findByVehicleId(UUID vehicleId);

  List<Inspection> findByStatus(InspectionStatus status);

  @Query(
      "SELECT i FROM Inspection i WHERE "
          + "(:buyerId IS NULL OR i.buyer.id = :buyerId) AND "
          + "(:sellerId IS NULL OR i.seller.id = :sellerId) AND "
          + "(:inspectorId IS NULL OR i.inspector.id = :inspectorId) AND "
          + "(:vehicleId IS NULL OR i.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR i.status = :status)")
  List<Inspection> search(
      @Param("buyerId") UUID buyerId,
      @Param("sellerId") UUID sellerId,
      @Param("inspectorId") UUID inspectorId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") InspectionStatus status);

  @Query(
      "SELECT i FROM Inspection i WHERE "
          + "(:buyerId IS NULL OR i.buyer.id = :buyerId) AND "
          + "(:sellerId IS NULL OR i.seller.id = :sellerId) AND "
          + "(:inspectorId IS NULL OR i.inspector.id = :inspectorId) AND "
          + "(:vehicleId IS NULL OR i.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR i.status = :status)")
  Page<Inspection> searchPageable(
      @Param("buyerId") UUID buyerId,
      @Param("sellerId") UUID sellerId,
      @Param("inspectorId") UUID inspectorId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") InspectionStatus status,
      Pageable pageable);

  Page<Inspection> findByBuyerId(UUID buyerId, Pageable pageable);

  Page<Inspection> findBySellerId(UUID sellerId, Pageable pageable);

  Page<Inspection> findByInspectorId(UUID inspectorId, Pageable pageable);

  Page<Inspection> findByVehicleId(UUID vehicleId, Pageable pageable);

  Page<Inspection> findByStatus(InspectionStatus status, Pageable pageable);
}

