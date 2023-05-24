package com.synergy.http.impl.netty;

import com.synergy.api.session.Session;
import com.synergy.api.session.SessionHandler;
import com.synergy.api.session.exception.SessionException;
import com.synergy.http.request.RequestContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

public class NettyHttpMethodHandler<T extends Session> {
  public static final String COOKIE_SESSION_HEADER = "SYN_SES";
  public final Logger logger;
  private final SessionHandler<T> sessionHandler;
  @Getter
  private final Map<String, NettyHttpMethod> methods = new HashMap<>();

  public NettyHttpMethodHandler(SessionHandler<T> sessionHandler, Logger logger) {
    this.sessionHandler = sessionHandler;
    this.logger = logger;
  }

  public FullHttpResponse processRequestObject(final RequestContext requestContext) {

    try {

      NettyHttpMethod nettyHttpMethod = methods.get(requestContext.getPath());
      if (nettyHttpMethod == null) {
        return NettyHttpResponseUtils
            .simpleHttpText404("Requested url does not have a registered service at this path.");
      }

      String httpMethodName =
          requestContext.getHttpMethod().name();
      requestContext.setUriParams(
          NettyHttpRequestUtils.transformParams(requestContext.getUri()));

      if (!httpMethodName.equalsIgnoreCase(nettyHttpMethod.httpMethod())) {
        return NettyHttpResponseUtils
            .simpleHttpText404("Requested service does not support the given http method.");
      }


      Method method = nettyHttpMethod.methodToCall();
      if (nettyHttpMethod.needSession()) {
        Cookie loginCookie = null;

        for (Cookie cookie : requestContext.getCookies()) {
          if (cookie.name().equalsIgnoreCase(COOKIE_SESSION_HEADER)) {
            loginCookie = cookie;
          }
        }

        if (loginCookie == null) {
          return NettyHttpResponseUtils.simpleHttpText404("No fitting cookie header sent!");
        }

        T session;
        try {
          session = sessionHandler.getSessionAndCache(loginCookie.value());

        } catch (SessionException e) {
          logger.log(Level.WARNING, e.getMessage());
          return NettyHttpResponseUtils.simpleHttpText404(e.getMessage());
        }


        return (FullHttpResponse) method.invoke(nettyHttpMethod.service(),
            requestContext, session);
      }

      return (FullHttpResponse) method.invoke(nettyHttpMethod.service(),
          requestContext);

    } catch (RuntimeException | InvocationTargetException | IllegalAccessException e) {
      String message = e.getMessage() == null ? "null" : e.getMessage();
      logger.log(Level.WARNING,message);
      e.printStackTrace();
      return NettyHttpResponseUtils.simpleHttpText404(message);
    }
  }


}
