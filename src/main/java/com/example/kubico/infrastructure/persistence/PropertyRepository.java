package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.Property;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
