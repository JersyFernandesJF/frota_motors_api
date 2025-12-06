package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Financing;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.FinancingCreateDTO;
import com.example.frotamotors.infrastructure.dto.FinancingResponseDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancingMapper {

  private FinancingMapper() {}

  public static FinancingResponseDTO toResponse(Financing financing) {
    return new FinancingResponseDTO(
        financing.getId(),
        VehicleMapper.toResponse(financing.getVehicle()),
        UserMapper.toResponse(financing.getBuyer()),
        UserMapper.toResponse(financing.getSeller()),
        financing.getStatus(),
        financing.getVehiclePrice(),
        financing.getFinancingAmount().add(financing.getDownPayment()), // requestedAmount
        financing.getFinancingAmount(), // approvedAmount (same as financingAmount for now)
        financing.getDownPayment(),
        financing.getFinancingAmount(),
        financing.getInterestRate(),
        financing.getLoanTermMonths(),
        financing.getMonthlyPayment(),
        financing.getCreditScore(),
        financing.getCreditScoreSimulation(),
        financing.getRejectionReason(),
        null, // notes - not stored in entity yet
        financing.getApprovedBy() != null ? UserMapper.toResponse(financing.getApprovedBy()) : null,
        financing.getApprovedAt(),
        financing.getRejectedBy() != null ? UserMapper.toResponse(financing.getRejectedBy()) : null,
        financing.getRejectedAt(),
        financing.getCreatedAt(),
        financing.getUpdatedAt());
  }

  public static Financing toEntity(
      FinancingCreateDTO dto, Vehicle vehicle, User buyer, User seller) {
    Financing financing = new Financing();
    financing.setVehicle(vehicle);
    financing.setBuyer(buyer);
    financing.setSeller(seller);
    financing.setVehiclePrice(dto.vehiclePrice());
    financing.setDownPayment(dto.downPayment());
    financing.setInterestRate(dto.interestRate());
    financing.setLoanTermMonths(dto.loanTermMonths());

    // Calculate financing amount
    BigDecimal financingAmount = dto.vehiclePrice().subtract(dto.downPayment());
    financing.setFinancingAmount(financingAmount);

    // Calculate monthly payment using compound interest formula
    // M = P * [r(1+r)^n] / [(1+r)^n - 1]
    BigDecimal monthlyRate =
        dto.interestRate()
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
    BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
    BigDecimal onePlusRPowerN = onePlusR.pow(dto.loanTermMonths());
    BigDecimal numerator = monthlyRate.multiply(onePlusRPowerN);
    BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
    BigDecimal monthlyPayment =
        financingAmount.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    financing.setMonthlyPayment(monthlyPayment);

    return financing;
  }
}
