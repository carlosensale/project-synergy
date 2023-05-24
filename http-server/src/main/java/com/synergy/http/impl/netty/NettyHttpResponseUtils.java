package com.synergy.http.impl.netty;

import com.synergy.http.misc.HttpResponseBuilder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * A netty utility class. It holds predefined responses
 */
public class NettyHttpResponseUtils {

  /**
   * Simple http text 200 full http response.
   *
   * @param content the content that will be sent 
   * @return the predefined response
   */
public static FullHttpResponse simpleHttpText200(final String content) {
    return new HttpResponseBuilder().content(content).build();
  }

  /**
   * Simple http text 404 full http response.
   *
   * @param content the content that will be sent 
   * @return the predefined response
   */
  public static FullHttpResponse simpleHttpText404(final String content) {
    return new HttpResponseBuilder().status(HttpResponseStatus.NOT_FOUND).content(content)
        .build();
  }

}
