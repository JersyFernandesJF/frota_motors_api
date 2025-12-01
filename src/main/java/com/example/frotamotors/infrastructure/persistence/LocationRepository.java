package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.Location;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

  @Query(
      "SELECT DISTINCT l.city FROM Location l "
          + "WHERE (:search IS NULL OR LOWER(l.city) LIKE LOWER(CONCAT('%', :search, '%'))) "
          + "ORDER BY l.city ASC")
  List<String> findCitySuggestions(@Param("search") String search);
}
