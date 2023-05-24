package com.synergy.http.impl.netty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A netty utility class
 */
class NettyHttpRequestUtils {

  private NettyHttpRequestUtils() {
    throw new UnsupportedOperationException("Static usage only.");
  }

  /**
   * Transform an uri path and decode the parameter
   *
   * @param input the input
   * @return a key-value map with all parameters from a uri
   */
  static Map<String, String> transformParams(final String input) {
    if (input.isEmpty() || input.isBlank() || !input.contains("?")) {
      return new HashMap<>();
    }
    final String paramsString = input.substring(input.indexOf('?') + 1);

    // in case of duplicated params, will be used la last value
    return Arrays.stream(
            paramsString.contains("&") ? paramsString.split("&") : new String[] {paramsString})
        .map(value -> value.split("="))
        .collect(Collectors.toMap(paramValue -> paramValue[0], paramValue -> paramValue[1],
            (v1, v2) -> v2));
  }

}
