package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.ListingModerationStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Vehicle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
  List<Vehicle> findByOwnerId(UUID ownerId);

  List<Vehicle> findByAgencyId(UUID agencyId);

  List<Vehicle> findByType(VehicleType type);

  List<Vehicle> findByStatus(VehicleStatus status);

  List<Vehicle> findByTypeAndStatus(VehicleType type, VehicleStatus status);

  @Query(
      "SELECT v FROM Vehicle v WHERE "
          + "(:type IS NULL OR v.type = :type) AND "
          + "(:status IS NULL OR v.status = :status) AND "
          + "(:minPrice IS NULL OR v.price >= :minPrice) AND "
          + "(:maxPrice IS NULL OR v.price <= :maxPrice) AND "
          + "(:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND "
          + "(:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND "
          + "(:minYear IS NULL OR v.year >= :minYear) AND "
          + "(:maxYear IS NULL OR v.year <= :maxYear) AND "
          + "(:fuelType IS NULL OR LOWER(v.fuelType) LIKE LOWER(CONCAT('%', :fuelType, '%')))")
  List<Vehicle> search(
      @Param("type") VehicleType type,
      @Param("status") VehicleStatus status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("model") String model,
      @Param("minYear") Integer minYear,
      @Param("maxYear") Integer maxYear,
      @Param("fuelType") String fuelType);

  @Query(
      "SELECT v FROM Vehicle v WHERE "
          + "(:type IS NULL OR v.type = :type) AND "
          + "(:status IS NULL OR v.status = :status) AND "
          + "(:minPrice IS NULL OR v.price >= :minPrice) AND "
          + "(:maxPrice IS NULL OR v.price <= :maxPrice) AND "
          + "(:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND "
          + "(:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND "
          + "(:minYear IS NULL OR v.year >= :minYear) AND "
          + "(:maxYear IS NULL OR v.year <= :maxYear) AND "
          + "(:fuelType IS NULL OR LOWER(v.fuelType) LIKE LOWER(CONCAT('%', :fuelType, '%')))")
  Page<Vehicle> searchPageable(
      @Param("type") VehicleType type,
      @Param("status") VehicleStatus status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("model") String model,
      @Param("minYear") Integer minYear,
      @Param("maxYear") Integer maxYear,
      @Param("fuelType") String fuelType,
      Pageable pageable);

  Page<Vehicle> findByOwnerId(UUID ownerId, Pageable pageable);

  Page<Vehicle> findByAgencyId(UUID agencyId, Pageable pageable);

  Page<Vehicle> findByType(VehicleType type, Pageable pageable);

  Page<Vehicle> findByStatus(VehicleStatus status, Pageable pageable);

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.createdAt >= :date")
  Long countByCreatedAtAfter(@Param("date") LocalDateTime date);

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.createdAt BETWEEN :start AND :end")
  Long countByCreatedAtBetween(
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT v.type, COUNT(v) FROM Vehicle v GROUP BY v.type")
  List<Object[]> countByType();

  @Query(
      value =
          "SELECT brand, COUNT(*) as count FROM vehicles GROUP BY brand ORDER BY count DESC LIMIT 5",
      nativeQuery = true)
  List<Object[]> findTopBrands();

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.owner.id = :ownerId AND v.status = 'FOR_SALE'")
  Long countActiveByOwnerId(@Param("ownerId") UUID ownerId);

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.owner.id = :ownerId")
  Long countTotalByOwnerId(@Param("ownerId") UUID ownerId);

  Long countByModerationStatus(ListingModerationStatus moderationStatus);
}
