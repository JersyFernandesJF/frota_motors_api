package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.ReviewType;
import com.example.frotamotors.domain.model.Review;
import com.example.frotamotors.domain.service.ReviewService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.ReviewCreateDTO;
import com.example.frotamotors.infrastructure.dto.ReviewResponseDTO;
import com.example.frotamotors.infrastructure.mapper.ReviewMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

  @Autowired private ReviewService reviewService;

  @GetMapping
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getAll(pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reviewer/{reviewerId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewer(
      @PathVariable UUID reviewerId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewer(reviewerId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByType(
      @PathVariable ReviewType type,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByType(type, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewedUser(
      @PathVariable UUID userId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewedUser(userId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/vehicle/{vehicleId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewedVehicle(
      @PathVariable UUID vehicleId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewedVehicle(vehicleId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/part/{partId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewedPart(
      @PathVariable UUID partId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewedPart(partId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/agency/{agencyId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewedAgency(
      @PathVariable UUID agencyId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewedAgency(agencyId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/rental/{rentalId}")
  public ResponseEntity<PageResponseDTO<ReviewResponseDTO>> getByReviewedRental(
      @PathVariable UUID rentalId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Review> page = reviewService.getByReviewedRental(rentalId, pageable);

    List<ReviewResponseDTO> content =
        page.getContent().stream().map(ReviewMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<ReviewResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/rating/{type}/{entityId}")
  public ResponseEntity<Double> getAverageRating(
      @PathVariable ReviewType type, @PathVariable UUID entityId) {
    Double rating = reviewService.getAverageRating(type, entityId);
    return ResponseEntity.ok(rating != null ? rating : 0.0);
  }

  @GetMapping("/count/{type}/{entityId}")
  public ResponseEntity<Long> getReviewCount(
      @PathVariable ReviewType type, @PathVariable UUID entityId) {
    Long count = reviewService.getReviewCount(type, entityId);
    return ResponseEntity.ok(count != null ? count : 0L);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReviewResponseDTO> getById(@PathVariable UUID id) {
    Review review = reviewService.getById(id);
    return ResponseEntity.ok(ReviewMapper.toResponse(review));
  }

  @PostMapping
  public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewCreateDTO dto) {
    Review saved = reviewService.create(dto);
    return ResponseEntity.ok(ReviewMapper.toResponse(saved));
  }

  @PutMapping("{id}")
  public ResponseEntity<ReviewResponseDTO> update(
      @PathVariable UUID id, @RequestBody ReviewCreateDTO dto) {
    Review updated = reviewService.update(id, dto);
    return ResponseEntity.ok(ReviewMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    reviewService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
