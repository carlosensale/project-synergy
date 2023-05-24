package com.synergy.api.session.impl;

import com.synergy.api.database.DataHandler;
import com.synergy.api.database.Filter;
import com.synergy.api.database.exception.DataHandlerException;
import com.synergy.api.session.Session;
import com.synergy.api.session.SessionHandler;
import com.synergy.api.session.exception.SessionException;
import com.synergy.api.session.exception.SessionExpiredException;
import com.synergy.api.session.exception.SessionNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DefaultSessionHandler<T extends Session> implements SessionHandler<T> {

  // todo may add a redis implementation
  private final Map<String, T> cachedSessions = new ConcurrentHashMap<>();
  private final DataHandler<T> sessionDatabase;
  private final Logger logger;

  public DefaultSessionHandler(DataHandler<T> sessionDatabase, Logger logger) {
    this.sessionDatabase = sessionDatabase;
    this.logger = logger;
  }

  @Override
  public T getSessionAndCache(String cookie) throws SessionException {

    T result = cachedSessions.get(cookie);

    if (result == null) {

      try {

        Optional<T> fetched = sessionDatabase.fetch(Filter.of("cookie", cookie));

        if (fetched.isEmpty()) {
          throw new SessionNotFoundException("No session found with the cookie '" + cookie + "'.");
        }

        result = fetched.get();
      } catch (DataHandlerException e) {
        throw new SessionNotFoundException(
            "Cannot fetch the session from the database for cookie '" + cookie + "'");
      }

      if (checkForExpiration(result)) {
        throw new SessionExpiredException(cookie);
      }

      cachedSessions.put(cookie, result);
    } else {
      if (checkForExpiration(result)) {
        throw new SessionExpiredException(cookie);
      }
    }

    return result;
  }

  @Override
  public void removeFromCache(String cookie) {
    cachedSessions.remove(cookie);
  }

  @Override
  public void registerSession(T t) throws SessionException {

    T result;

    try {
      result = sessionDatabase.store(t);
    } catch (DataHandlerException e) {
      throw new SessionException("Unable to store session to database.");
    }

    cachedSessions.put(result.getCookie(), result);
  }

  private boolean checkForExpiration(T session) {
    if (session.getTimeout() < System.currentTimeMillis()) {
      String cookie = session.getCookie();
      logger.info("The cookie '" + cookie + "' is expired! Deleting it...");
      removeFromCache(cookie);
      sessionDatabase.delete(Filter.of("cookie", cookie));
      return true;
    }
    return false;
  }

  @Override
  public void deleteSession(String cookie) {
    removeFromCache(cookie);
    sessionDatabase.delete(Filter.of("cookie", cookie));
  }

  @Override
  public void close() {
    cachedSessions.clear();
  }
}
