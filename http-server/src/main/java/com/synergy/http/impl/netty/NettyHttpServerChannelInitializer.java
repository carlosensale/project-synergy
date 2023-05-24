package com.synergy.http.impl.netty;

import com.synergy.api.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class NettyHttpServerChannelInitializer<S extends Session> extends ChannelInitializer<Channel> {

  private final NettyHttpMethodHandler<S> nettyHttpMethodHandler;

  @Override
  protected void initChannel(Channel channel) {
    channel
        .pipeline()
        .addLast(new HttpServerCodec())
        .addLast(new NettyHttpServerInboundHandler<S>(nettyHttpMethodHandler));
  }
}
