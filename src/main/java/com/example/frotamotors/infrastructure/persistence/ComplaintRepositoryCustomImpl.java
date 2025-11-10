package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ComplaintRepositoryCustomImpl implements ComplaintRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Double getAverageResponseTime(ComplaintStatus status) {
    String sql =
        "SELECT AVG((EXTRACT(EPOCH FROM resolved_at) - EXTRACT(EPOCH FROM created_at)) / 3600.0) "
            + "FROM complaints WHERE status = CAST(:status AS VARCHAR) AND resolved_at IS NOT NULL";

    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("status", status.name());

    Object result = query.getSingleResult();
    return result != null ? ((Number) result).doubleValue() : null;
  }
}

