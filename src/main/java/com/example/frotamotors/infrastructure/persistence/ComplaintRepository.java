package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.ComplaintPriority;
import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import com.example.frotamotors.domain.model.Complaint;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository
    extends JpaRepository<Complaint, UUID>, ComplaintRepositoryCustom {
  List<Complaint> findByReporterId(UUID reporterId);

  List<Complaint> findByStatus(ComplaintStatus status);

  List<Complaint> findByType(ComplaintType type);

  List<Complaint> findByReportedUserId(UUID reportedUserId);

  List<Complaint> findByReportedVehicleId(UUID reportedVehicleId);

  List<Complaint> findByReportedPartId(UUID reportedPartId);

  List<Complaint> findByReportedPropertyId(UUID reportedPropertyId);

  List<Complaint> findByReportedAgencyId(UUID reportedAgencyId);

  @Query(
      "SELECT c FROM Complaint c WHERE "
          + "(:status IS NULL OR c.status = :status) AND "
          + "(:type IS NULL OR c.type = :type) AND "
          + "(:reporterId IS NULL OR c.reporter.id = :reporterId)")
  List<Complaint> search(
      @Param("status") ComplaintStatus status,
      @Param("type") ComplaintType type,
      @Param("reporterId") UUID reporterId);

  @Query(
      "SELECT c FROM Complaint c WHERE "
          + "(:status IS NULL OR c.status = :status) AND "
          + "(:type IS NULL OR c.type = :type) AND "
          + "(:reporterId IS NULL OR c.reporter.id = :reporterId)")
  Page<Complaint> searchPageable(
      @Param("status") ComplaintStatus status,
      @Param("type") ComplaintType type,
      @Param("reporterId") UUID reporterId,
      Pageable pageable);

  Page<Complaint> findByReporterId(UUID reporterId, Pageable pageable);

  Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);

  Page<Complaint> findByType(ComplaintType type, Pageable pageable);

  Page<Complaint> findByReportedUserId(UUID reportedUserId, Pageable pageable);

  Page<Complaint> findByReportedVehicleId(UUID reportedVehicleId, Pageable pageable);

  Page<Complaint> findByReportedPartId(UUID reportedPartId, Pageable pageable);

  Page<Complaint> findByReportedPropertyId(UUID reportedPropertyId, Pageable pageable);

  Page<Complaint> findByReportedAgencyId(UUID reportedAgencyId, Pageable pageable);

  Long countByStatus(ComplaintStatus status);

  Long countByType(ComplaintType type);

  Long countByPriority(ComplaintPriority priority);
}
