package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.Property;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
  List<Property>
      findByAreaM2BetweenAndTypeInAndBathroomsGreaterThanEqualAndRoomsGreaterThanEqualAndTotalFloorsGreaterThanEqualAndYearBuiltGreaterThanEqual(
          Double minArea,
          Double maxArea,
          List<String> types,
          Integer bathrooms,
          Integer rooms,
          Integer floors,
          Integer year);

  @Query(
      "SELECT p FROM Property p WHERE "
          + "(:minArea IS NULL OR p.areaM2 >= :minArea) AND "
          + "(:maxArea IS NULL OR p.areaM2 <= :maxArea) AND "
          + "(:types IS NULL OR p.type IN :types) AND "
          + "(:bathrooms IS NULL OR p.bathrooms >= :bathrooms) AND "
          + "(:rooms IS NULL OR p.rooms >= :rooms) AND "
          + "(:floors IS NULL OR p.totalFloors >= :floors) AND "
          + "(:year IS NULL OR p.yearBuilt >= :year)")
  Page<Property> searchPageable(
      @Param("minArea") Double minArea,
      @Param("maxArea") Double maxArea,
      @Param("types") List<String> types,
      @Param("bathrooms") Integer bathrooms,
      @Param("rooms") Integer rooms,
      @Param("floors") Integer floors,
      @Param("year") Integer year,
      Pageable pageable);
}
