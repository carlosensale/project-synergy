package com.synergy.api.database;

import dev.morphia.query.experimental.filters.Filters;
import lombok.AllArgsConstructor;

/**
 * A utility class for filtering database actions.
 */
@AllArgsConstructor
public class Filter {
  /**
   * creates a new filter with the given key and value
   *
   * @param key field that will be filtered
   * @param value that will be searched
   * @return a new filter without calling the constructor
   */
public static Filter of(String key, Object value) {
    return new Filter(key, value);
  }

  private final String key;
  private final Object value;

  /**
   * Adapts a filter to a morphia filter
   *
   * @return a fitting morphia filter
   */
public dev.morphia.query.experimental.filters.Filter toBsonFilter(){
    return Filters.eq(key,value);
  }
  /**
   * The key of the filter
   *
   * @return the key as a string
   */
public String key(){
    return key;
  }
  /**
   * The value of the filter
   *
   * @return the value as a string with single quotation marks
   */
  public String value() {

    if(value instanceof String){
      return "'"+value+"'";
    }

      return String.valueOf(value);
  }

  @Override
  public String toString() {
    return "Filter{" +
        "key='" + key + '\'' +
        ", value=" + value +
        '}';
  }
}
