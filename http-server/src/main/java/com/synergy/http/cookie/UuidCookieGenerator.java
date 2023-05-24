package com.synergy.http.cookie;

import java.util.UUID;

public class UuidCookieGenerator implements CookieGenerator {

  @Override
  public String generateCookie() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
