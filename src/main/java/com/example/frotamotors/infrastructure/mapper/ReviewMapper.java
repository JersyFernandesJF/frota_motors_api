package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Review;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleRental;
import com.example.frotamotors.infrastructure.dto.ReviewCreateDTO;
import com.example.frotamotors.infrastructure.dto.ReviewResponseDTO;

public class ReviewMapper {

  private ReviewMapper() {}

  public static ReviewResponseDTO toResponse(Review review) {
    return new ReviewResponseDTO(
        review.getId(),
        review.getReviewer() != null ? UserMapper.toResponse(review.getReviewer()) : null,
        review.getType(),
        review.getReviewedUser() != null ? UserMapper.toResponse(review.getReviewedUser()) : null,
        review.getReviewedVehicle() != null
            ? VehicleMapper.toResponse(review.getReviewedVehicle())
            : null,
        review.getReviewedPart() != null ? PartMapper.toResponse(review.getReviewedPart()) : null,
        review.getReviewedAgency() != null
            ? AgencyMapper.toResponse(review.getReviewedAgency())
            : null,
        review.getReviewedRental() != null
            ? VehicleRentalMapper.toResponse(review.getReviewedRental())
            : null,
        review.getRating(),
        review.getComment(),
        review.getCreatedAt(),
        review.getUpdatedAt());
  }

  public static Review toEntity(
      ReviewCreateDTO dto,
      User reviewer,
      User reviewedUser,
      Vehicle reviewedVehicle,
      Part reviewedPart,
      Agency reviewedAgency,
      VehicleRental reviewedRental) {
    Review review = new Review();
    review.setReviewer(reviewer);
    review.setType(dto.type());
    review.setRating(dto.rating());
    review.setComment(dto.comment());
    review.setReviewedUser(reviewedUser);
    review.setReviewedVehicle(reviewedVehicle);
    review.setReviewedPart(reviewedPart);
    review.setReviewedAgency(reviewedAgency);
    review.setReviewedRental(reviewedRental);
    return review;
  }
}
