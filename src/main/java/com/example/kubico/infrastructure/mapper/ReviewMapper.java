package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Review;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.domain.model.VehicleRental;
import com.example.kubico.infrastructure.dto.ReviewCreateDTO;
import com.example.kubico.infrastructure.dto.ReviewResponseDTO;

public class ReviewMapper {

  private ReviewMapper() {}

  public static ReviewResponseDTO toResponse(Review review) {
    return new ReviewResponseDTO(
        review.getId(),
        review.getReviewer(),
        review.getType(),
        review.getReviewedUser(),
        review.getReviewedVehicle(),
        review.getReviewedPart(),
        review.getReviewedAgency(),
        review.getReviewedRental(),
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

