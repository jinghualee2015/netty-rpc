package com.netty.rpc.protocol.handler;

import com.netty.rpc.core.RpcRequest;
import com.netty.rpc.core.RpcResponse;
import com.netty.rpc.core.RpcServiceHelper;
import com.netty.rpc.protocol.protocol.MessageHeader;
import com.netty.rpc.protocol.protocol.MessageStatus;
import com.netty.rpc.protocol.protocol.MessageType;
import com.netty.rpc.protocol.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            MessageHeader header = msg.getHeader();
            header.setMsgType((byte) MessageType.RESPONSE.getType());
            try {
                Object result = handle(msg.getBody());
                response.setData(result);
                header.setStatus((byte) MessageStatus.SUCCESS.getCode());
            } catch (Throwable throwable) {
                header.setStatus((byte) MessageStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {}, error", header.getRequestId(), throwable);

            }
            resProtocol.setHeader(header);
            resProtocol.setBody(response);
            ctx.writeAndFlush(resProtocol);
        });
    }

    /**
     * 处理请求
     *
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handle(RpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);
        if (null == serviceBean) {
            throw new RuntimeException(String.format("service Not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();
        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
