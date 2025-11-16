package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.PaymentMethod;
import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.domain.model.Payment;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  List<Payment> findByPayerId(UUID payerId);

  List<Payment> findByPurchaseId(UUID purchaseId);

  List<Payment> findByStatus(PaymentStatus status);

  List<Payment> findByMethod(PaymentMethod method);

  @Query(
      "SELECT p FROM Payment p WHERE "
          + "(:payerId IS NULL OR p.payer.id = :payerId) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:method IS NULL OR p.method = :method)")
  List<Payment> search(
      @Param("payerId") UUID payerId,
      @Param("status") PaymentStatus status,
      @Param("method") PaymentMethod method);

  @Query(
      "SELECT p FROM Payment p WHERE "
          + "(:payerId IS NULL OR p.payer.id = :payerId) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:method IS NULL OR p.method = :method)")
  Page<Payment> searchPageable(
      @Param("payerId") UUID payerId,
      @Param("status") PaymentStatus status,
      @Param("method") PaymentMethod method,
      Pageable pageable);

  Page<Payment> findByPayerId(UUID payerId, Pageable pageable);

  Page<Payment> findByPurchaseId(UUID purchaseId, Pageable pageable);

  Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

  Long countByStatus(PaymentStatus status);

  @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
  BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);
}
