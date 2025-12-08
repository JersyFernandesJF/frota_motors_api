package com.example.frotamotors.infrastructure.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to map RSQL field names to JPA entity field names.
 *
 * <p>This allows using frontend-friendly field names in RSQL queries that map to actual entity
 * properties.
 */
public class RsqlFieldMapper {

  private static final Pattern FIELD_PATTERN = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_.]*)");

  /**
   * Maps RSQL field names in a filter string using the provided mapping function.
   *
   * @param rsqlFilter the original RSQL filter string
   * @param fieldMapper function that maps field names
   * @return the RSQL filter string with mapped field names
   */
  public static String mapFields(String rsqlFilter, Function<String, String> fieldMapper) {
    if (rsqlFilter == null || rsqlFilter.trim().isEmpty()) {
      return rsqlFilter;
    }

    StringBuffer result = new StringBuffer();
    Matcher matcher = FIELD_PATTERN.matcher(rsqlFilter);

    while (matcher.find()) {
      String originalField = matcher.group(1);
      String mappedField = fieldMapper.apply(originalField);
      matcher.appendReplacement(result, Matcher.quoteReplacement(mappedField));
    }
    matcher.appendTail(result);

    return result.toString();
  }

  /**
   * Creates a field mapper for Vehicle entity.
   *
   * <p>Maps common frontend field names to entity properties:
   *
   * <ul>
   *   <li>mileage -> mileageKm
   *   <li>createdAt -> createdAt
   *   <li>updatedAt -> updatedAt
   * </ul>
   *
   * @return a function that maps field names for Vehicle
   */
  public static Function<String, String> vehicleFieldMapper() {
    Map<String, String> fieldMap = new HashMap<>();
    fieldMap.put("mileage", "mileageKm");
    // Add more mappings as needed
    // fieldMap.put("frontendField", "entityField");

    return field -> fieldMap.getOrDefault(field, field);
  }

  /**
   * Creates a field mapper for Part entity.
   *
   * @return a function that maps field names for Part
   */
  public static Function<String, String> partFieldMapper() {
    Map<String, String> fieldMap = new HashMap<>();
    // Add mappings as needed
    return field -> fieldMap.getOrDefault(field, field);
  }

  /**
   * Creates a field mapper for User entity.
   *
   * @return a function that maps field names for User
   */
  public static Function<String, String> userFieldMapper() {
    Map<String, String> fieldMap = new HashMap<>();
    // Add mappings as needed
    return field -> fieldMap.getOrDefault(field, field);
  }

  /**
   * Creates a default field mapper that returns the field name as-is.
   *
   * @return a function that returns the field name unchanged
   */
  public static Function<String, String> defaultFieldMapper() {
    return Function.identity();
  }
}
