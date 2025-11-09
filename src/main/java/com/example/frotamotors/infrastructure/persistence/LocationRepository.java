package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {}
