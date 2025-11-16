package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import com.example.frotamotors.domain.model.Purchase;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
  List<Purchase> findByBuyerId(UUID buyerId);

  List<Purchase> findByStatus(OrderStatus status);

  List<Purchase> findByType(OrderType type);

  List<Purchase> findByVehicleId(UUID vehicleId);

  List<Purchase> findByPartId(UUID partId);

  @Query(
      "SELECT p FROM Purchase p WHERE "
          + "(:buyerId IS NULL OR p.buyer.id = :buyerId) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:type IS NULL OR p.type = :type)")
  List<Purchase> search(
      @Param("buyerId") UUID buyerId,
      @Param("status") OrderStatus status,
      @Param("type") OrderType type);

  @Query(
      "SELECT p FROM Purchase p WHERE "
          + "(:buyerId IS NULL OR p.buyer.id = :buyerId) AND "
          + "(:status IS NULL OR p.status = :status) AND "
          + "(:type IS NULL OR p.type = :type)")
  Page<Purchase> searchPageable(
      @Param("buyerId") UUID buyerId,
      @Param("status") OrderStatus status,
      @Param("type") OrderType type,
      Pageable pageable);

  Page<Purchase> findByBuyerId(UUID buyerId, Pageable pageable);

  Page<Purchase> findByStatus(OrderStatus status, Pageable pageable);

  Page<Purchase> findByType(OrderType type, Pageable pageable);
}
