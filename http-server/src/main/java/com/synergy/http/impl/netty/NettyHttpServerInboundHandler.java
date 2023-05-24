package com.synergy.http.impl.netty;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.synergy.api.session.Session;
import com.synergy.http.request.RequestContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class NettyHttpServerInboundHandler<S extends Session> extends SimpleChannelInboundHandler<HttpObject> {

  private final static ThreadLocal<RequestContext> localRequestContext = new ThreadLocal<>();
  private final NettyHttpMethodHandler<S> httpMethodHandler;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) {

    if (msg instanceof final HttpRequest request) {
      final RequestContext context = new RequestContext();

      context.setHttpMethod(request.method());

      context.setUriAndPath(URLDecoder.decode(request.uri(), StandardCharsets.UTF_8));

      context.setHeaders(request.headers().entries().stream().collect(
          Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

      localRequestContext.set(context);

    } else if (msg instanceof LastHttpContent) {

      final ByteBuf jsonBuf = ((LastHttpContent) msg).content();
      RequestContext requestContext = localRequestContext.get();

      if (jsonBuf.readableBytes() > 0) {
        try {

          requestContext.setBody(
              JsonParser.parseString(jsonBuf.toString(StandardCharsets.UTF_8)).getAsJsonObject());

        } catch (JsonSyntaxException e) {
          e.printStackTrace();
          channelHandlerContext.write(NettyHttpResponseUtils.simpleHttpText404(
              "The message body must contain a valid json context."));
          return;
        }
      }

      localRequestContext.remove();
      channelHandlerContext.write(httpMethodHandler.processRequestObject(requestContext));
    }

  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }
}
