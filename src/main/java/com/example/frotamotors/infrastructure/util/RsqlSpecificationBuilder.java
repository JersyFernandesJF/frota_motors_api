package com.example.frotamotors.infrastructure.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import io.github.perplexhub.rsql.RSQLJPASupport;

/**
 * Utility class to build JPA Specifications from RSQL filter strings.
 *
 * <p>RSQL (RESTful Service Query Language) allows dynamic querying using a simple syntax:
 *
 * <ul>
 *   <li>Equality: field==value
 *   <li>Inequality: field!=value
 *   <li>Greater than: field=gt=value
 *   <li>Greater or equal: field=ge=value
 *   <li>Less than: field=lt=value
 *   <li>Less or equal: field=le=value
 *   <li>In: field=in=(value1,value2)
 *   <li>Not in: field=out=(value1,value2)
 *   <li>Like: field=like=value (case-sensitive)
 *   <li>Case-insensitive like: field=ilike=value
 *   <li>AND: field1==value1;field2==value2
 *   <li>OR: field1==value1,field2==value2
 * </ul>
 *
 * <p>Example: type==CAR;price>=10000;price<=50000;brand==Toyota
 */
@Component
public class RsqlSpecificationBuilder {

  /**
   * Builds a JPA Specification from an RSQL filter string.
   *
   * @param rsqlFilter the RSQL filter string (e.g., "type==CAR;price>=10000")
   * @param entityClass the entity class to query
   * @param <T> the entity type
   * @return a Specification that can be used with JpaRepository.findAll()
   */
  public <T> Specification<T> build(String rsqlFilter, Class<T> entityClass) {
    if (rsqlFilter == null || rsqlFilter.trim().isEmpty()) {
      // Return a specification that matches all (no filtering)
      return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    try {
      // RSQLJPASupport.toSpecification expects a String, not a Node
      return RSQLJPASupport.toSpecification(rsqlFilter);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Invalid RSQL filter: " + rsqlFilter + ". Error: " + e.getMessage(), e);
    }
  }

  /**
   * Builds a JPA Specification from an RSQL filter string with field mapping.
   *
   * @param rsqlFilter the RSQL filter string
   * @param entityClass the entity class to query
   * @param fieldMapper function to map RSQL field names to entity field names
   * @param <T> the entity type
   * @return a Specification that can be used with JpaRepository.findAll()
   */
  public <T> Specification<T> build(
      String rsqlFilter, Class<T> entityClass, java.util.function.Function<String, String> fieldMapper) {
    if (rsqlFilter == null || rsqlFilter.trim().isEmpty()) {
      // Return a specification that matches all (no filtering)
      return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    // Apply field mapping to the RSQL string
    String mappedFilter = RsqlFieldMapper.mapFields(rsqlFilter, fieldMapper);

    return build(mappedFilter, entityClass);
  }
}

