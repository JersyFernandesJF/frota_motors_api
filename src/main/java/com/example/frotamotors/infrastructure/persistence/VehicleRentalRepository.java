package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.RentalStatus;
import com.example.frotamotors.domain.model.VehicleRental;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRentalRepository extends JpaRepository<VehicleRental, UUID> {
  List<VehicleRental> findByRenterId(UUID renterId);

  List<VehicleRental> findByVehicleId(UUID vehicleId);

  List<VehicleRental> findByAgencyId(UUID agencyId);

  List<VehicleRental> findByStatus(RentalStatus status);

  @Query(
      "SELECT vr FROM VehicleRental vr WHERE "
          + "(:renterId IS NULL OR vr.renter.id = :renterId) AND "
          + "(:vehicleId IS NULL OR vr.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR vr.status = :status) AND "
          + "(:startDate IS NULL OR vr.startDate >= :startDate) AND "
          + "(:endDate IS NULL OR vr.endDate <= :endDate)")
  List<VehicleRental> search(
      @Param("renterId") UUID renterId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") RentalStatus status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(
      "SELECT vr FROM VehicleRental vr WHERE "
          + "(:renterId IS NULL OR vr.renter.id = :renterId) AND "
          + "(:vehicleId IS NULL OR vr.vehicle.id = :vehicleId) AND "
          + "(:status IS NULL OR vr.status = :status) AND "
          + "(:startDate IS NULL OR vr.startDate >= :startDate) AND "
          + "(:endDate IS NULL OR vr.endDate <= :endDate)")
  Page<VehicleRental> searchPageable(
      @Param("renterId") UUID renterId,
      @Param("vehicleId") UUID vehicleId,
      @Param("status") RentalStatus status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      Pageable pageable);

  Page<VehicleRental> findByRenterId(UUID renterId, Pageable pageable);

  Page<VehicleRental> findByVehicleId(UUID vehicleId, Pageable pageable);

  Page<VehicleRental> findByAgencyId(UUID agencyId, Pageable pageable);

  Page<VehicleRental> findByStatus(RentalStatus status, Pageable pageable);

  @Query(
      "SELECT vr FROM VehicleRental vr WHERE "
          + "vr.vehicle.id = :vehicleId AND "
          + "vr.status IN ('PENDING', 'ACTIVE') AND "
          + "((vr.startDate <= :endDate AND vr.endDate >= :startDate))")
  List<VehicleRental> findConflictingRentals(
      @Param("vehicleId") UUID vehicleId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}

