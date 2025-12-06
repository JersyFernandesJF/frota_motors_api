package com.example.frotamotors.infrastructure.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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

  private static final RSQLParser RSQL_PARSER = new RSQLParser();

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
      Node rootNode = RSQL_PARSER.parse(rsqlFilter);
      return createSpecification(rootNode, entityClass);
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

  @SuppressWarnings("unchecked")
  private <T> Specification<T> createSpecification(Node node, Class<T> entityClass) {
    if (node instanceof LogicalNode logicalNode) {
      return createLogicalSpecification(logicalNode, entityClass);
    } else if (node instanceof ComparisonNode comparisonNode) {
      return createComparisonSpecification(comparisonNode, entityClass);
    }
    return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
  }

  private <T> Specification<T> createLogicalSpecification(LogicalNode node, @SuppressWarnings("unused") Class<T> entityClass) {
    List<Specification<T>> specs =
        node.getChildren().stream()
            .map(child -> createSpecification(child, entityClass))
            .collect(Collectors.toList());

    if (node instanceof AndNode) {
      return Specification.allOf(specs);
    } else if (node instanceof OrNode) {
      return Specification.anyOf(specs);
    }
    return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
  }

  private <T> Specification<T> createComparisonSpecification(
      ComparisonNode node, Class<T> entityClass) {
    return (root, query, criteriaBuilder) -> {
      String fieldName = node.getSelector();
      ComparisonOperator operator = node.getOperator();
      List<String> arguments = node.getArguments();

      Path<?> fieldPath = getFieldPath(root, fieldName);
      Class<?> fieldType = fieldPath.getJavaType();

      return buildPredicate(criteriaBuilder, fieldPath, operator, arguments, fieldType);
    };
  }

  private Path<?> getFieldPath(Root<?> root, String fieldName) {
    String[] parts = fieldName.split("\\.");
    Path<?> path = root;
    for (String part : parts) {
      path = path.get(part);
    }
    return path;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Predicate buildPredicate(
      CriteriaBuilder criteriaBuilder,
      Path<?> fieldPath,
      ComparisonOperator operator,
      List<String> arguments,
      Class<?> fieldType) {
    String operatorSymbol = operator.getSymbol();

    switch (operatorSymbol) {
      case "==":
        return criteriaBuilder.equal(fieldPath, convertValue(arguments.get(0), fieldType));
      case "!=":
        return criteriaBuilder.notEqual(fieldPath, convertValue(arguments.get(0), fieldType));
      case "=gt=":
        return criteriaBuilder.greaterThan(
            (Path<Comparable>) fieldPath, (Comparable) convertValue(arguments.get(0), fieldType));
      case "=ge=":
        return criteriaBuilder.greaterThanOrEqualTo(
            (Path<Comparable>) fieldPath, (Comparable) convertValue(arguments.get(0), fieldType));
      case "=lt=":
        return criteriaBuilder.lessThan(
            (Path<Comparable>) fieldPath, (Comparable) convertValue(arguments.get(0), fieldType));
      case "=le=":
        return criteriaBuilder.lessThanOrEqualTo(
            (Path<Comparable>) fieldPath, (Comparable) convertValue(arguments.get(0), fieldType));
      case "=in=":
        return fieldPath.in(
            arguments.stream()
                .map(arg -> convertValue(arg, fieldType))
                .collect(Collectors.toList()));
      case "=out=":
        return criteriaBuilder.not(
            fieldPath.in(
                arguments.stream()
                    .map(arg -> convertValue(arg, fieldType))
                    .collect(Collectors.toList())));
      case "=like=":
        return criteriaBuilder.like(
            (Path<String>) fieldPath, "%" + arguments.get(0) + "%");
      case "=ilike=":
        return criteriaBuilder.like(
            criteriaBuilder.lower((Path<String>) fieldPath),
            "%" + arguments.get(0).toLowerCase() + "%");
      default:
        throw new IllegalArgumentException("Unsupported operator: " + operatorSymbol);
    }
  }

  private Object convertValue(String value, Class<?> targetType) {
    if (targetType == String.class) {
      return value;
    } else if (targetType == Integer.class || targetType == int.class) {
      return Integer.parseInt(value);
    } else if (targetType == Long.class || targetType == long.class) {
      return Long.parseLong(value);
    } else if (targetType == Double.class || targetType == double.class) {
      return Double.parseDouble(value);
    } else if (targetType == BigDecimal.class) {
      return new BigDecimal(value);
    } else if (targetType == Boolean.class || targetType == boolean.class) {
      return Boolean.parseBoolean(value);
    } else if (targetType == LocalDateTime.class) {
      return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } else if (targetType.isEnum()) {
      @SuppressWarnings({"unchecked", "rawtypes"})
      Class<Enum> enumClass = (Class<Enum>) targetType;
      return Enum.valueOf(enumClass, value);
    }
    return value;
  }
}

