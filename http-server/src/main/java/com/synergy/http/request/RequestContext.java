package com.synergy.http.request;

import com.google.gson.JsonObject;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * The Request context that will be provided in every registered path.
 */
public class RequestContext {
  private static final ServerCookieDecoder cookieDecoder = ServerCookieDecoder.LAX;
  private static final JsonObject EMPTY = new JsonObject();

  @Getter
  @Setter
  private HttpMethod httpMethod;
  @Getter
  private String uri;

  @Getter
  private String path;
  @Getter
  @Setter
  private Map<String, String> uriParams;
  @Getter
  private Map<String, String> headers;
  @Getter
  @Setter
  private Set<Cookie> cookies = new HashSet<>();
  @Getter
  @Setter
  private JsonObject body = EMPTY;

  /**
   * Sets uri and path by removing the question mark
   *
   * @param uri the uri that is called in the request
   */
public void setUriAndPath(String uri) {
    this.uri = uri;
    if (uri.contains("?")) {
      uri = uri.substring(0, uri.indexOf('?'));
    }

    this.path = uri;
  }

  /**
   * Sets the headers and decodes the cookies
   *
   * @param headers a map that came from the http header
   */
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;

    String cookieHeader = headers.get("Cookie");
    if (cookieHeader != null) {
      cookies = cookieDecoder.decode(cookieHeader);
    }
  }
}