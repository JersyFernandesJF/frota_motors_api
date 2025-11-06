package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.enums.PartCategory;
import com.example.kubico.domain.enums.PartStatus;
import com.example.kubico.domain.model.Part;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID> {
  List<Part> findBySellerId(UUID sellerId);

  List<Part> findByAgencyId(UUID agencyId);

  List<Part> findByCategory(PartCategory category);

  List<Part> findByStatus(PartStatus status);

  List<Part> findByCategoryAndStatus(PartCategory category, PartStatus status);

  @Query(
      "SELECT p FROM Part p WHERE "
          + "(:category IS NULL OR p.category = :category) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
          + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
          + "(:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND "
          + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
          + "(:partNumber IS NULL OR LOWER(p.partNumber) LIKE LOWER(CONCAT('%', :partNumber, '%'))) AND "
          + "(:oemNumber IS NULL OR LOWER(p.oemNumber) LIKE LOWER(CONCAT('%', :oemNumber, '%')))")
  List<Part> search(
      @Param("category") PartCategory category,
      @Param("status") PartStatus status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("name") String name,
      @Param("partNumber") String partNumber,
      @Param("oemNumber") String oemNumber);

  @Query(
      "SELECT p FROM Part p WHERE "
          + "(:category IS NULL OR p.category = :category) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
          + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
          + "(:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND "
          + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
          + "(:partNumber IS NULL OR LOWER(p.partNumber) LIKE LOWER(CONCAT('%', :partNumber, '%'))) AND "
          + "(:oemNumber IS NULL OR LOWER(p.oemNumber) LIKE LOWER(CONCAT('%', :oemNumber, '%')))")
  Page<Part> searchPageable(
      @Param("category") PartCategory category,
      @Param("status") PartStatus status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("name") String name,
      @Param("partNumber") String partNumber,
      @Param("oemNumber") String oemNumber,
      Pageable pageable);

  Page<Part> findBySellerId(UUID sellerId, Pageable pageable);

  Page<Part> findByAgencyId(UUID agencyId, Pageable pageable);

  Page<Part> findByCategory(PartCategory category, Pageable pageable);

  Page<Part> findByStatus(PartStatus status, Pageable pageable);
}

