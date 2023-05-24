package com.synergy.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation needed to create a method that will be registered in a http server
 * This annotation can only be applied on methods and is available during the runtime
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethodHandler {
  /**
   * The http method that will be used, e.g. POST, GET, PUT
   *
   * @return the method as string
   */
String method();

  /**
   * The path that will be mapped to the method
   *
   * @return the path as string
   */
  String path();

  /**
   * Represent a flag if the requested method at the requested path needs to have a session.
   * If the flag is true, a specific cookie header is required.
   *
   * @return the flag if a session is needed
   */
  boolean session() default true;
}
