package com.netty.rpc.protocol.codec;

import com.netty.rpc.core.RpcRequest;
import com.netty.rpc.core.RpcResponse;
import com.netty.rpc.protocol.protocol.MessageHeader;
import com.netty.rpc.protocol.protocol.MessageType;
import com.netty.rpc.protocol.protocol.ProtocolConstants;
import com.netty.rpc.protocol.protocol.RpcProtocol;
import com.netty.rpc.protocol.serialization.RpcSerialization;
import com.netty.rpc.protocol.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 2byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEAD_TOTAL_LEN) {
            return;
        }
        in.markReaderIndex();

        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        byte version = in.readByte();
        byte serialization = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int msgLen = in.readInt();

        if (in.readableBytes() < msgLen) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[msgLen];
        in.readBytes(data);
        MessageType msgTypeEnum = MessageType.findType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serialization);
        header.setRequestId(requestId);
        header.setStatus(status);
        header.setMsgType(msgType);
        header.setMsgLen(msgLen);

        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(serialization);
        switch (msgTypeEnum) {
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                break;

        }

    }
}
