package com.netty.rpc.consumer;

import com.netty.rpc.core.RpcRequest;
import com.netty.rpc.core.RpcServiceHelper;
import com.netty.rpc.core.ServiceMeta;
import com.netty.rpc.protocol.codec.RpcDecoder;
import com.netty.rpc.protocol.codec.RpcEncoder;
import com.netty.rpc.protocol.handler.RpcResponseHandler;
import com.netty.rpc.protocol.protocol.RpcProtocol;
import com.netty.rpc.registry.RegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Slf4j
public class RpcConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final static Map<String, ChannelFuture> CONNECTION_MAP = new ConcurrentHashMap<>();
    private final static RpcConsumer INSTANCE = new RpcConsumer();

    public static RpcConsumer getInstance() {
        return INSTANCE;
    }

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(2);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (eventLoopGroup != null) {
                log.info("client event loop group has shutdown gracefully. ");
                eventLoopGroup.shutdownGracefully();
            }
        }, "client-hook"));
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        RpcRequest request = protocol.getBody();
        Object[] params = request.getParams();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        int invokeHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokeHashCode);
        if (null != serviceMeta) {
            ChannelFuture future = getChannelFuture(serviceMeta);
            future.channel().writeAndFlush(protocol);
        }
    }

    private ChannelFuture getChannelFuture(ServiceMeta serviceMeta) throws InterruptedException {
        String serviceProviderKey = serviceMeta.getServiceAddr() + ":" + serviceMeta.getServicePort();
        ChannelFuture future = CONNECTION_MAP.get(serviceProviderKey);
        if (null == future) {
            synchronized (this) {
                future = CONNECTION_MAP.get(serviceProviderKey);
                if (null == future) {
                    final ChannelFuture fFuture = bootstrap.connect(serviceMeta.getServiceAddr(), serviceMeta.getServicePort()).sync();
                    fFuture.addListener((ChannelFutureListener) listener -> {
                        if (fFuture.isSuccess()) {
                            log.info("connect rpc server {} on port {} success.", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                        } else {
                            log.error("connect rpc server {} on port {} failed.", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                            fFuture.cause().printStackTrace();
                        }
                    });
                    future = fFuture;
                    CONNECTION_MAP.put(serviceProviderKey, future);
                }
            }
        }
        return future;
    }
}
