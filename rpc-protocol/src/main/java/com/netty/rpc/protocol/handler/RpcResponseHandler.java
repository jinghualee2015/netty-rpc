package com.netty.rpc.protocol.handler;

import com.netty.rpc.core.RpcFuture;
import com.netty.rpc.core.RpcRequestHolder;
import com.netty.rpc.core.RpcResponse;
import com.netty.rpc.protocol.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        long requestId = msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.get(requestId);
        future.getPromised().setSuccess(msg.getBody());
    }
}
