package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.VehicleHistory;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, UUID> {
  Page<VehicleHistory> findByVehicleIdOrderByChangedAtDesc(UUID vehicleId, Pageable pageable);
}
