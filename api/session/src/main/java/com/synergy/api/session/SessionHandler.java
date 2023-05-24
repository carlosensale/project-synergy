package com.synergy.api.session;

import com.synergy.api.session.exception.SessionException;
import java.io.Closeable;

/**
 * A SessionHandler manage the session that will be used while the application is running.
 *
 * @param <T>  the type of the session. It can include more than just the necessary fields.
 *             For example a user instance.
 */
public interface SessionHandler<T extends Session> extends Closeable {

  /**
   * Register session in the database and add it to the cache.
   *
   * @param t the instance of the class t
   * @throws SessionException or a Sub-class, when an error occurred while registering a session
   */
void registerSession(T t) throws SessionException;

  /**
   * Gets a session. If the session is not in the cache, it will be retrieved from the database
   * and put into the cache for the next get.
   *
   * @param cookie that the session needs to fit
   * @return the session that is associated with the given cookie
   * @throws SessionException or a Sub-class, when an error occurred while getting a session
   */
T getSessionAndCache(String cookie) throws SessionException;
  /**
   * Remove a session from the cache.
   *
   * @param cookie that the session needs to fit
   */
void removeFromCache(String cookie);
  /**
   * Delete a session from the database. If it is cached, it will be removed from the cache.
   *
   * @param cookie that the session needs to fit
   */
  void deleteSession(String cookie);
}
