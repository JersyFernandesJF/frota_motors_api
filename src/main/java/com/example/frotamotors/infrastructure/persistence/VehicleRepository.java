package com.example.frotamotors.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.frotamotors.domain.enums.ListingModerationStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Vehicle;

@Repository
public interface VehicleRepository
    extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {
  List<Vehicle> findByOwnerId(UUID ownerId);

  List<Vehicle> findByAgencyId(UUID agencyId);

  List<Vehicle> findByType(VehicleType type);

  List<Vehicle> findByStatus(VehicleStatus status);

  List<Vehicle> findByTypeAndStatus(VehicleType type, VehicleStatus status);

  @Query(
      value =
          "SELECT DISTINCT v.* FROM vehicles v WHERE "
              + "(:type IS NULL OR v.type = :type) AND "
              + "(:status IS NULL OR v.status = :status) AND "
              + "(:minPrice IS NULL OR v.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR v.price <= :maxPrice) AND "
              + "(:brand IS NULL OR LOWER(v.brand::text) LIKE LOWER('%' || :brand || '%')) AND "
              + "(:model IS NULL OR LOWER(v.model::text) LIKE LOWER('%' || :model || '%')) AND "
              + "(:minYear IS NULL OR v.year >= :minYear) AND "
              + "(:maxYear IS NULL OR v.year <= :maxYear) AND "
              + "(:fuelType IS NULL OR LOWER(v.fuel_type::text) LIKE LOWER('%' || :fuelType || '%'))",
      nativeQuery = true)
  List<Vehicle> search(
      @Param("type") String type,
      @Param("status") String status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("model") String model,
      @Param("minYear") Integer minYear,
      @Param("maxYear") Integer maxYear,
      @Param("fuelType") String fuelType);

  @Query(
      value =
          "SELECT v.* FROM vehicles v WHERE "
              + "(CAST(:type AS TEXT) IS NULL OR v.type = CAST(:type AS TEXT)) AND "
              + "(CAST(:status AS TEXT) IS NULL OR v.status = CAST(:status AS TEXT)) AND "
              + "(:minPrice IS NULL OR v.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR v.price <= :maxPrice) AND "
              + "(CAST(:brand AS TEXT) IS NULL OR LOWER(v.brand::text) LIKE LOWER('%' || CAST(:brand AS TEXT) || '%')) AND "
              + "(CAST(:model AS TEXT) IS NULL OR LOWER(v.model::text) LIKE LOWER('%' || CAST(:model AS TEXT) || '%')) AND "
              + "(:minYear IS NULL OR v.year >= :minYear) AND "
              + "(:maxYear IS NULL OR v.year <= :maxYear) AND "
              + "(CAST(:fuelType AS TEXT) IS NULL OR v.fuel_type::text = CAST(:fuelType AS TEXT)) AND "
              + "(CAST(:transmission AS TEXT) IS NULL OR v.transmission_type::text = CAST(:transmission AS TEXT)) AND "
              + "(:minMileage IS NULL OR v.mileage_km >= :minMileage) AND "
              + "(:maxMileage IS NULL OR v.mileage_km <= :maxMileage) AND "
              + "(:startDate IS NULL OR v.created_at >= :startDate) AND "
              + "(:endDate IS NULL OR v.created_at <= :endDate) AND "
              + "(CAST(:search AS TEXT) IS NULL OR "
              + "LOWER(v.brand::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%') OR "
              + "LOWER(v.model::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%') OR "
              + "LOWER(v.description::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%'))",
      countQuery =
          "SELECT COUNT(*) FROM vehicles v WHERE "
              + "(CAST(:type AS TEXT) IS NULL OR v.type = CAST(:type AS TEXT)) AND "
              + "(CAST(:status AS TEXT) IS NULL OR v.status = CAST(:status AS TEXT)) AND "
              + "(:minPrice IS NULL OR v.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR v.price <= :maxPrice) AND "
              + "(CAST(:brand AS TEXT) IS NULL OR LOWER(v.brand::text) LIKE LOWER('%' || CAST(:brand AS TEXT) || '%')) AND "
              + "(CAST(:model AS TEXT) IS NULL OR LOWER(v.model::text) LIKE LOWER('%' || CAST(:model AS TEXT) || '%')) AND "
              + "(:minYear IS NULL OR v.year >= :minYear) AND "
              + "(:maxYear IS NULL OR v.year <= :maxYear) AND "
              + "(CAST(:fuelType AS TEXT) IS NULL OR v.fuel_type::text = CAST(:fuelType AS TEXT)) AND "
              + "(CAST(:transmission AS TEXT) IS NULL OR v.transmission_type::text = CAST(:transmission AS TEXT)) AND "
              + "(:minMileage IS NULL OR v.mileage_km >= :minMileage) AND "
              + "(:maxMileage IS NULL OR v.mileage_km <= :maxMileage) AND "
              + "(:startDate IS NULL OR v.created_at >= :startDate) AND "
              + "(:endDate IS NULL OR v.created_at <= :endDate) AND "
              + "(CAST(:search AS TEXT) IS NULL OR "
              + "LOWER(v.brand::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%') OR "
              + "LOWER(v.model::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%') OR "
              + "LOWER(v.description::text) LIKE LOWER('%' || CAST(:search AS TEXT) || '%'))",
      nativeQuery = true)
  Page<Vehicle> searchPageable(
      @Param("type") String type,
      @Param("status") String status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("model") String model,
      @Param("minYear") Integer minYear,
      @Param("maxYear") Integer maxYear,
      @Param("fuelType") String fuelType,
      @Param("transmission") String transmission,
      @Param("minMileage") Integer minMileage,
      @Param("maxMileage") Integer maxMileage,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("search") String search,
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
          "SELECT LOWER(v.brand::text) as brand, COUNT(*) as count FROM vehicles v "
              + "WHERE (:search IS NULL OR LOWER(v.brand::text) LIKE LOWER('%' || :search || '%')) "
              + "GROUP BY LOWER(v.brand::text) ORDER BY COUNT(*) DESC",
      nativeQuery = true)
  List<Object[]> findBrandsWithCount(@Param("search") String search);

  @Query(
      value =
          "SELECT LOWER(v.model::text) as model, COUNT(*) as count FROM vehicles v "
              + "WHERE (:brand IS NULL OR LOWER(v.brand::text) = LOWER(:brand)) "
              + "AND (:search IS NULL OR LOWER(v.model::text) LIKE LOWER('%' || :search || '%')) "
              + "GROUP BY LOWER(v.model::text) ORDER BY COUNT(*) DESC",
      nativeQuery = true)
  List<Object[]> findModelsWithCount(@Param("brand") String brand, @Param("search") String search);

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

  @Query(
      "SELECT DISTINCT v FROM Vehicle v LEFT JOIN FETCH v.media LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.agency WHERE v.id = :id")
  Optional<Vehicle> findByIdWithMedia(@Param("id") UUID id);

  @Query(
      value =
          "SELECT DISTINCT v FROM Vehicle v LEFT JOIN FETCH v.media LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.agency",
      countQuery = "SELECT COUNT(DISTINCT v) FROM Vehicle v")
  Page<Vehicle> findAllWithMedia(Pageable pageable);

  @Query(
      "SELECT COALESCE(SUM(v.price), 0) FROM Vehicle v WHERE v.status = 'FOR_SALE' AND v.moderationStatus = 'APPROVED'")
  BigDecimal sumPriceByActiveStatus();

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.price >= :minPrice AND v.price < :maxPrice")
  Long countByPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

  @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.price >= :minPrice")
  Long countByPriceGreaterThanEqual(@Param("minPrice") BigDecimal minPrice);

  @Query(
      "SELECT COALESCE(SUM(v.price), 0) FROM Vehicle v WHERE v.createdAt >= :startDate AND v.createdAt <= :endDate")
  BigDecimal sumPriceByCreatedAtBetween(
      @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
