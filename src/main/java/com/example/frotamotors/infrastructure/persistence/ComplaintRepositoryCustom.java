package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.ComplaintStatus;

public interface ComplaintRepositoryCustom {
  Double getAverageResponseTime(ComplaintStatus status);
}
