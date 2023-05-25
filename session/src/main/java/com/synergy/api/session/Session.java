package com.synergy.api.session;
/**
 * The interface for every session. Can be extended with more fields by implementing this interface.
 */
public interface Session {

  /**
   * The cookie that will be mapped to this session.
   *
   * @return the cookie as a string without the cookie header name. Only the value.
   */
  String getCookie();

  /**
   * The unique id of the session.
   *
   * @return the id as a long.
   */
  long getId();

  /**
   * A timestamp when the session expires.
   *
   * @return the timestamp as a long in unix timestamp.
   */
  long getTimeout();
}
