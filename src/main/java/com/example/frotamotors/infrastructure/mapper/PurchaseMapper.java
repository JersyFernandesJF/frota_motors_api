package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.PurchaseCreateDTO;
import com.example.frotamotors.infrastructure.dto.PurchaseResponseDTO;
import java.math.BigDecimal;

public class PurchaseMapper {

  private PurchaseMapper() {}

  public static PurchaseResponseDTO toResponse(Purchase purchase) {
    return new PurchaseResponseDTO(
        purchase.getId(),
        purchase.getBuyer(),
        purchase.getType(),
        purchase.getStatus(),
        purchase.getVehicle(),
        purchase.getPart(),
        purchase.getPrice(),
        purchase.getCurrency(),
        purchase.getQuantity(),
        purchase.getTotalAmount(),
        purchase.getShippingAddress(),
        purchase.getDeliveryDate(),
        purchase.getTrackingNumber(),
        purchase.getNotes(),
        purchase.getCreatedAt(),
        purchase.getUpdatedAt());
  }

  public static Purchase toEntity(
      PurchaseCreateDTO dto, User buyer, Vehicle vehicle, Part part) {
    Purchase purchase = new Purchase();
    purchase.setBuyer(buyer);
    purchase.setType(dto.type());
    purchase.setStatus(OrderStatus.PENDING);
    purchase.setVehicle(vehicle);
    purchase.setPart(part);
    purchase.setQuantity(dto.quantity() != null ? dto.quantity() : 1);

    // Set price and currency based on the item
    if (vehicle != null) {
      purchase.setPrice(vehicle.getPrice());
      purchase.setCurrency(vehicle.getCurrency());
    } else if (part != null) {
      purchase.setPrice(part.getPrice());
      purchase.setCurrency(part.getCurrency());
    }

    // Calculate total amount
    BigDecimal itemPrice = purchase.getPrice();
    int qty = purchase.getQuantity();
    purchase.setTotalAmount(itemPrice.multiply(BigDecimal.valueOf(qty)));

    purchase.setShippingAddress(dto.shippingAddress());
    purchase.setNotes(dto.notes());

    return purchase;
  }
}

