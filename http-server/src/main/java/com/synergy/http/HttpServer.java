package com.synergy.http;

import java.util.Collection;

/**
 * A http service that can registered services.
 */
public interface HttpServer {

  /**
   * Start http server on a given host a port.
   *
   * @param host the host for the server
   * @param port the port for the server
   * @throws Exception will be thrown if an error occurred while startup
   */
void startHttpServer(String host, int port) throws Exception;

  void close();

  /**
   * Register a http-service with its own http-methods.
   *
   * @param httpService the http service
   * @return the http server to concat the expressions
   */
HttpServer registerService(HttpService httpService);

  /**
   * Gets all registered http services.
   *
   * @return a collection of http services
   */
  Collection<HttpService> getHttpServices();
}
