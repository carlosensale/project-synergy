package com.synergy.http.cookie;

/**
 * An interface for every Sub-class that will generate cookies
 */
public interface CookieGenerator {

  /**
   * Generate a cookie in an implemented way
   *
   * @return a cookie as string without the identifier for the header
   */
  String generateCookie();
}
