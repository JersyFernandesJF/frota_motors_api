package com.example.kubico.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

  @Override
  public void initialize(StrongPassword constraintAnnotation) {}

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null || password.isEmpty()) {
      return false;
    }

    // At least 8 characters
    if (password.length() < 8) {
      return false;
    }

    // At least one uppercase letter
    if (!password.matches(".*[A-Z].*")) {
      return false;
    }

    // At least one lowercase letter
    if (!password.matches(".*[a-z].*")) {
      return false;
    }

    // At least one digit
    if (!password.matches(".*[0-9].*")) {
      return false;
    }

    return true;
  }
}

