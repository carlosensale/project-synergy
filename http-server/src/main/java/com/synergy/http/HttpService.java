package com.synergy.http;

/**
 * A markup interface for all http services that will be registered The implementing class needs to
 * hold methods with this schema:
 * @HttpMethodHandler(path="/v1/user/api/login method="POST" session=true)
 * public FullHttpResponse simpleExample(RequestContext context, Session session){
 *  ...
 * }
 *
 * <p>The method needs a HttpMethodHandler annotation with the parameters path, method and session.
 * The first parameter needs to be a RequestContext If the session flag is true, a second method
 * parameter of the type Session is needed. As Return type is a FullHttpResponse (from Netty)
 * required.
 */
public interface HttpService {

  /**
   * The name of the http service for identifying the service.
   *
   * @return the name of the service
   */
  String getName();
}
