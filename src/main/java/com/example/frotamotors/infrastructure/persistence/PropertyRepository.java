package com.example.frotamotors.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.frotamotors.domain.model.Property;

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
      value = "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.media LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.agency WHERE "
          + "(:minArea IS NULL OR p.areaM2 >= :minArea) AND "
          + "(:maxArea IS NULL OR p.areaM2 <= :maxArea) AND "
          + "(:types IS NULL OR p.type IN :types) AND "
          + "(:bathrooms IS NULL OR p.bathrooms >= :bathrooms) AND "
          + "(:rooms IS NULL OR p.rooms >= :rooms) AND "
          + "(:floors IS NULL OR p.totalFloors >= :floors) AND "
          + "(:year IS NULL OR p.yearBuilt >= :year)",
      countQuery = "SELECT COUNT(DISTINCT p) FROM Property p WHERE "
          + "(:minArea IS NULL OR p.areaM2 >= :minArea) AND "
          + "(:maxArea IS NULL OR p.areaM2 <= :maxArea) AND "
          + "(:types IS NULL OR p.type IN :types) AND "
          + "(:bathrooms IS NULL OR p.bathrooms >= :bathrooms) AND "
          + "(:rooms IS NULL OR p.rooms >= :rooms) AND "
          + "(:floors IS NULL OR p.totalFloors >= :floors) AND "
          + "(:year IS NULL OR p.yearBuilt >= :year)")
  Page<Property> searchPageableWithMedia(
      @Param("minArea") Double minArea,
      @Param("maxArea") Double maxArea,
      @Param("types") List<String> types,
      @Param("bathrooms") Integer bathrooms,
      @Param("rooms") Integer rooms,
      @Param("floors") Integer floors,
      @Param("year") Integer year,
      Pageable pageable);

  @Query(
      value = "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.media LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.agency",
      countQuery = "SELECT COUNT(DISTINCT p) FROM Property p")
  Page<Property> findAllWithMedia(Pageable pageable);

  @Query("SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.media LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.agency WHERE p.id = :id")
  Optional<Property> findByIdWithMedia(@Param("id") UUID id);

  @Query(
      "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.media LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.agency WHERE "
          + "p.areaM2 BETWEEN :minArea AND :maxArea AND "
          + "p.type IN :types AND "
          + "p.bathrooms >= :bathrooms AND "
          + "p.rooms >= :rooms AND "
          + "p.totalFloors >= :floors AND "
          + "p.yearBuilt >= :year")
  List<Property> searchWithMedia(
      @Param("minArea") Double minArea,
      @Param("maxArea") Double maxArea,
      @Param("types") List<String> types,
      @Param("bathrooms") Integer bathrooms,
      @Param("rooms") Integer rooms,
      @Param("floors") Integer floors,
      @Param("year") Integer year);
}
