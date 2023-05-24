package com.synergy.api.session.exception;

public class SessionExpiredException extends SessionException {

  public SessionExpiredException(String cookie) {
    super("The requested session '" + cookie + "' is expired!");
  }
}
