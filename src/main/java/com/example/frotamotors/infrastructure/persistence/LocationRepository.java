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
      value =
          "SELECT DISTINCT l.city::text FROM locations l "
              + "WHERE (:search IS NULL OR LOWER(l.city::text) LIKE LOWER('%' || :search || '%')) "
              + "ORDER BY l.city::text ASC",
      nativeQuery = true)
  List<String> findCitySuggestions(@Param("search") String search);
}
