package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.enums.ReviewType;
import com.example.kubico.domain.model.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
  List<Review> findByReviewerId(UUID reviewerId);

  Page<Review> findByReviewerId(UUID reviewerId, Pageable pageable);

  List<Review> findByType(ReviewType type);

  Page<Review> findByType(ReviewType type, Pageable pageable);

  List<Review> findByReviewedUserId(UUID reviewedUserId);

  Page<Review> findByReviewedUserId(UUID reviewedUserId, Pageable pageable);

  List<Review> findByReviewedVehicleId(UUID reviewedVehicleId);

  Page<Review> findByReviewedVehicleId(UUID reviewedVehicleId, Pageable pageable);

  List<Review> findByReviewedPartId(UUID reviewedPartId);

  Page<Review> findByReviewedPartId(UUID reviewedPartId, Pageable pageable);

  List<Review> findByReviewedAgencyId(UUID reviewedAgencyId);

  Page<Review> findByReviewedAgencyId(UUID reviewedAgencyId, Pageable pageable);

  List<Review> findByReviewedRentalId(UUID reviewedRentalId);

  Page<Review> findByReviewedRentalId(UUID reviewedRentalId, Pageable pageable);

  @Query(
      "SELECT AVG(r.rating) FROM Review r WHERE "
          + "(:type = 'USER' AND r.reviewedUser.id = :entityId) OR "
          + "(:type = 'VEHICLE' AND r.reviewedVehicle.id = :entityId) OR "
          + "(:type = 'PART' AND r.reviewedPart.id = :entityId) OR "
          + "(:type = 'AGENCY' AND r.reviewedAgency.id = :entityId) OR "
          + "(:type = 'RENTAL' AND r.reviewedRental.id = :entityId)")
  Double getAverageRating(@Param("type") String type, @Param("entityId") UUID entityId);

  @Query(
      "SELECT COUNT(r) FROM Review r WHERE "
          + "(:type = 'USER' AND r.reviewedUser.id = :entityId) OR "
          + "(:type = 'VEHICLE' AND r.reviewedVehicle.id = :entityId) OR "
          + "(:type = 'PART' AND r.reviewedPart.id = :entityId) OR "
          + "(:type = 'AGENCY' AND r.reviewedAgency.id = :entityId) OR "
          + "(:type = 'RENTAL' AND r.reviewedRental.id = :entityId)")
  Long getReviewCount(@Param("type") String type, @Param("entityId") UUID entityId);
}

