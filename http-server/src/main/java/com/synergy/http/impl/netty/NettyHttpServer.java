package com.synergy.http.impl.netty;

import com.synergy.api.session.Session;
import com.synergy.api.session.SessionHandler;
import com.synergy.http.HttpServer;
import com.synergy.http.HttpService;
import com.synergy.http.annotations.HttpMethodHandler;
import com.synergy.http.request.RequestContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import lombok.Getter;


public class NettyHttpServer<S extends Session> implements HttpServer {

  @Getter
  private final Logger logger;
  private final NettyHttpMethodHandler<S> httpMethodHandler;
  @Getter
  private final Collection<HttpService> httpServices = new HashSet<>();
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;


  public NettyHttpServer(SessionHandler<S> sessionHandler, Logger logger) {
    this.logger = logger;
    httpMethodHandler = new NettyHttpMethodHandler<>(sessionHandler, logger);
  }

  @Override
  public void startHttpServer(String host, int port) throws Exception {
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new NettyHttpServerChannelInitializer<>(httpMethodHandler))
          .bind(host, port).channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }


  @Override
  public void close() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }

  @Override
  public HttpServer registerService(HttpService httpService) {

    for (Method method : httpService.getClass().getMethods()) {

      HttpMethodHandler methodAnno = method.getAnnotation(HttpMethodHandler.class);
      if (methodAnno == null) {
        continue;
      }

      if (!FullHttpResponse.class.isAssignableFrom(method.getReturnType())) {
        throw new IllegalArgumentException(String.format(
            "Error while registering method %s from service %s." +
                " Method must return a FullHttpResponse.", method.getName(),
            httpService.getName()));
      }

      boolean needSession = methodAnno.session();

      if (!needSession && (method.getParameterCount() != 1 ||
          !method.getParameterTypes()[0].equals(RequestContext.class))) {
        throw new IllegalArgumentException(String.format(
            "Error while registering method %s from service %s." +
                " Only a RequestObject is allowed as the only parameter.", method.getName(),
            httpService.getName()));
      }

      if (needSession && (method.getParameterCount() != 2 ||
          !method.getParameterTypes()[0].equals(RequestContext.class))) {
        throw new IllegalArgumentException(String.format(
            "Error while registering method %s from service %s." +
                " Only RequestContext and Session object is valid for this method.",
            method.getName(),
            httpService.getName()));
      }

      String path = methodAnno.path();
      String httpMethod = methodAnno.method();
      NettyHttpMethod nettyMethod = new NettyHttpMethod(httpMethod, httpService, method,
          needSession);
      logger.info("Mapping '" + method.getName() + "' to '" + path + "'");
      httpMethodHandler.getMethods().put(path, nettyMethod);
    }

    httpServices.add(httpService);
    return this;
  }
}
