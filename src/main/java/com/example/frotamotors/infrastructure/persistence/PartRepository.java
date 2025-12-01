package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.model.Part;
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
      value =
          "SELECT p.* FROM parts p WHERE "
              + "(:category IS NULL OR p.category = :category) AND "
              + "(:status IS NULL OR p.status = :status) AND "
              + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
              + "(:brand IS NULL OR LOWER(p.brand::text) LIKE LOWER('%' || :brand || '%')) AND "
              + "(:name IS NULL OR LOWER(p.name::text) LIKE LOWER('%' || :name || '%')) AND "
              + "(:partNumber IS NULL OR LOWER(p.part_number::text) LIKE LOWER('%' || :partNumber || '%')) AND "
              + "(:oemNumber IS NULL OR LOWER(p.oem_number::text) LIKE LOWER('%' || :oemNumber || '%'))",
      nativeQuery = true)
  List<Part> search(
      @Param("category") String category,
      @Param("status") String status,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("brand") String brand,
      @Param("name") String name,
      @Param("partNumber") String partNumber,
      @Param("oemNumber") String oemNumber);

  @Query(
      value =
          "SELECT p.* FROM parts p WHERE "
              + "(:category IS NULL OR p.category = :category) AND "
              + "(:status IS NULL OR p.status = :status) AND "
              + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
              + "(:brand IS NULL OR LOWER(p.brand::text) LIKE LOWER('%' || :brand || '%')) AND "
              + "(:name IS NULL OR LOWER(p.name::text) LIKE LOWER('%' || :name || '%')) AND "
              + "(:partNumber IS NULL OR LOWER(p.part_number::text) LIKE LOWER('%' || :partNumber || '%')) AND "
              + "(:oemNumber IS NULL OR LOWER(p.oem_number::text) LIKE LOWER('%' || :oemNumber || '%'))",
      countQuery =
          "SELECT COUNT(*) FROM parts p WHERE "
              + "(:category IS NULL OR p.category = :category) AND "
              + "(:status IS NULL OR p.status = :status) AND "
              + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
              + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
              + "(:brand IS NULL OR LOWER(p.brand::text) LIKE LOWER('%' || :brand || '%')) AND "
              + "(:name IS NULL OR LOWER(p.name::text) LIKE LOWER('%' || :name || '%')) AND "
              + "(:partNumber IS NULL OR LOWER(p.part_number::text) LIKE LOWER('%' || :partNumber || '%')) AND "
              + "(:oemNumber IS NULL OR LOWER(p.oem_number::text) LIKE LOWER('%' || :oemNumber || '%'))",
      nativeQuery = true)
  Page<Part> searchPageable(
      @Param("category") String category,
      @Param("status") String status,
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
