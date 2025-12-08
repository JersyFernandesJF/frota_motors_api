package com.example.frotamotors.infrastructure.validation;

import com.example.frotamotors.domain.enums.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

  @Override
  public void initialize(ValidCurrency constraintAnnotation) {}

  @Override
  public boolean isValid(String currency, ConstraintValidatorContext context) {
    if (currency == null || currency.isEmpty()) {
      return false;
    }

    try {
      Currency.valueOf(currency.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
