package com.synergy.http.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

/**
 * A builder for an easy creation of the http response
 */
public class HttpResponseBuilder {

  private static final ByteBuf EMPTY = Unpooled.EMPTY_BUFFER;
  private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
      .setPrettyPrinting().create();
  private final Map<AsciiString, String> header = new HashMap<>();
  private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
  private HttpResponseStatus status = HttpResponseStatus.OK;
  private String content;
  private String contentType = "text/plain";

  /**
   * sets the http version
   *
   * @param version the http version
   * @return the current builder
   */
//https://stackoverflow.com/questions/23714383/what-are-all-the-possible-values-for-http-content-type-header
  public HttpResponseBuilder version(HttpVersion version) {
    this.httpVersion = version;
    return this;
  }

  /**
   * Sets the http status
   *
   * @param status the status
   * @return the current builder
   */
public HttpResponseBuilder status(HttpResponseStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Sets the content as string
   *
   * @param content the content as string
   * @return the current builder
   */
public HttpResponseBuilder content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Changes the content type
   *
   * @param type as HttpContentType
   * @return the current builder
   */
public HttpResponseBuilder contentType(HttpContentType type) {
    this.content = type.toString();
    return this;
  }

  /**
   * Changes the content type, only use this when there is no fitting predefined HttpContentType
   *
   * @param type as string
   * @return the current builder
   */
public HttpResponseBuilder contentType(String type) {
    this.contentType = type;
    return this;
  }

  /**
   * Add a http header
   *
   * @param httpHeaderName the http header name
   * @param value the value as string
   * @return the current builder
   */
public HttpResponseBuilder addHeader(AsciiString httpHeaderName, String value) {
    header.put(httpHeaderName, value);
    return this;
  }

  /**
   * Add a http cookie header. No extra header needed
   *
   * @param cookie the cookie as string
   * @return the current builder
   */
public HttpResponseBuilder addCookie(String cookie) {
    header.put(HttpHeaderNames.SET_COOKIE, cookie);
    return this;
  }

  /**
   * Add content as object. It will be mapped by a GSON mapper to json content
   *
   * @param object the pojo object
   * @return the current builder
   */
public HttpResponseBuilder mapContent(Object object) {
    this.content = GSON.toJson(object);
    return this;
  }

  /**
   * Build the response
   *
   * @return a FullHttpResponse (Netty)
   */
public FullHttpResponse build() {

    ByteBuf con = EMPTY;

    if(content != null){
      con = Unpooled.wrappedBuffer(content.getBytes());
    }

    FullHttpResponse response = new DefaultFullHttpResponse(httpVersion, status,
        con);
    HttpHeaders httpHeaders = response.headers();
    httpHeaders.set(HttpHeaderNames.CONTENT_TYPE, contentType);
    httpHeaders.set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    header.forEach(httpHeaders::set);
    return response;
  }

  /**
   * Predefined content types
   */
@AllArgsConstructor
  public enum HttpContentType {

    /**
     *Json text
     */
JSON("application/json"),
    /**
     *Html text
     */
HTML("text/html"),

    /**
     *Xml text
     */
XML("text/xml"),

    /**
     *Png images
     */
PNG("image/png"),

    /**
     *Jpeg images
     */
JPEG("image/jpeg"),

    /**
     *Plain text
     */
PLAIN("text/plain");

    private final String value;

    @Override
    public String toString() {
      return value;
    }
  }

}
