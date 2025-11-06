package com.example.kubico.domain.service;

import com.example.kubico.domain.enums.ReviewType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Review;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.domain.model.VehicleRental;
import com.example.kubico.infrastructure.dto.ReviewCreateDTO;
import com.example.kubico.infrastructure.mapper.ReviewMapper;
import com.example.kubico.infrastructure.persistence.AgencyRepository;
import com.example.kubico.infrastructure.persistence.PartRepository;
import com.example.kubico.infrastructure.persistence.ReviewRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import com.example.kubico.infrastructure.persistence.VehicleRentalRepository;
import com.example.kubico.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  @Autowired private ReviewRepository reviewRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  @Autowired private AgencyRepository agencyRepository;

  @Autowired private VehicleRentalRepository vehicleRentalRepository;

  public Review create(ReviewCreateDTO dto) {
    User reviewer =
        userRepository
            .findById(dto.reviewerId())
            .orElseThrow(() -> new EntityNotFoundException("Reviewer not found"));

    User reviewedUser = null;
    Vehicle reviewedVehicle = null;
    Part reviewedPart = null;
    Agency reviewedAgency = null;
    VehicleRental reviewedRental = null;

    // Validate that exactly one reviewed entity is provided based on type
    int reviewedCount = 0;

    if (dto.reviewedUserId() != null) {
      reviewedUser =
          userRepository
              .findById(dto.reviewedUserId())
              .orElseThrow(() -> new EntityNotFoundException("Reviewed user not found"));
      reviewedCount++;
    }
    if (dto.reviewedVehicleId() != null) {
      reviewedVehicle =
          vehicleRepository
              .findById(dto.reviewedVehicleId())
              .orElseThrow(() -> new EntityNotFoundException("Reviewed vehicle not found"));
      reviewedCount++;
    }
    if (dto.reviewedPartId() != null) {
      reviewedPart =
          partRepository
              .findById(dto.reviewedPartId())
              .orElseThrow(() -> new EntityNotFoundException("Reviewed part not found"));
      reviewedCount++;
    }
    if (dto.reviewedAgencyId() != null) {
      reviewedAgency =
          agencyRepository
              .findById(dto.reviewedAgencyId())
              .orElseThrow(() -> new EntityNotFoundException("Reviewed agency not found"));
      reviewedCount++;
    }
    if (dto.reviewedRentalId() != null) {
      reviewedRental =
          vehicleRentalRepository
              .findById(dto.reviewedRentalId())
              .orElseThrow(() -> new EntityNotFoundException("Reviewed rental not found"));
      reviewedCount++;
    }

    if (reviewedCount != 1) {
      throw new IllegalArgumentException("Exactly one reviewed entity must be provided");
    }

    Review review =
        ReviewMapper.toEntity(
            dto, reviewer, reviewedUser, reviewedVehicle, reviewedPart, reviewedAgency,
            reviewedRental);
    return reviewRepository.save(review);
  }

  public List<Review> getAll() {
    return reviewRepository.findAll();
  }

  public Page<Review> getAll(Pageable pageable) {
    return reviewRepository.findAll(pageable);
  }

  public Review getById(UUID id) {
    return reviewRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Review not found"));
  }

  public Review update(UUID id, ReviewCreateDTO dto) {
    Review existing =
        reviewRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review not found"));

    existing.setRating(dto.rating());
    existing.setComment(dto.comment());

    return reviewRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!reviewRepository.existsById(id)) {
      throw new EntityNotFoundException("Review not found");
    }
    reviewRepository.deleteById(id);
  }

  public List<Review> getByReviewer(UUID reviewerId) {
    return reviewRepository.findByReviewerId(reviewerId);
  }

  public Page<Review> getByReviewer(UUID reviewerId, Pageable pageable) {
    return reviewRepository.findByReviewerId(reviewerId, pageable);
  }

  public List<Review> getByType(ReviewType type) {
    return reviewRepository.findByType(type);
  }

  public Page<Review> getByType(ReviewType type, Pageable pageable) {
    return reviewRepository.findByType(type, pageable);
  }

  public List<Review> getByReviewedUser(UUID reviewedUserId) {
    return reviewRepository.findByReviewedUserId(reviewedUserId);
  }

  public Page<Review> getByReviewedUser(UUID reviewedUserId, Pageable pageable) {
    return reviewRepository.findByReviewedUserId(reviewedUserId, pageable);
  }

  public List<Review> getByReviewedVehicle(UUID reviewedVehicleId) {
    return reviewRepository.findByReviewedVehicleId(reviewedVehicleId);
  }

  public Page<Review> getByReviewedVehicle(UUID reviewedVehicleId, Pageable pageable) {
    return reviewRepository.findByReviewedVehicleId(reviewedVehicleId, pageable);
  }

  public List<Review> getByReviewedPart(UUID reviewedPartId) {
    return reviewRepository.findByReviewedPartId(reviewedPartId);
  }

  public Page<Review> getByReviewedPart(UUID reviewedPartId, Pageable pageable) {
    return reviewRepository.findByReviewedPartId(reviewedPartId, pageable);
  }

  public List<Review> getByReviewedAgency(UUID reviewedAgencyId) {
    return reviewRepository.findByReviewedAgencyId(reviewedAgencyId);
  }

  public Page<Review> getByReviewedAgency(UUID reviewedAgencyId, Pageable pageable) {
    return reviewRepository.findByReviewedAgencyId(reviewedAgencyId, pageable);
  }

  public List<Review> getByReviewedRental(UUID reviewedRentalId) {
    return reviewRepository.findByReviewedRentalId(reviewedRentalId);
  }

  public Page<Review> getByReviewedRental(UUID reviewedRentalId, Pageable pageable) {
    return reviewRepository.findByReviewedRentalId(reviewedRentalId, pageable);
  }

  public Double getAverageRating(ReviewType type, UUID entityId) {
    return reviewRepository.getAverageRating(type.name(), entityId);
  }

  public Long getReviewCount(ReviewType type, UUID entityId) {
    return reviewRepository.getReviewCount(type.name(), entityId);
  }
}

