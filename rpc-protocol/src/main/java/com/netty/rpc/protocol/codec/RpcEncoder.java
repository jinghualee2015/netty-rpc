package com.netty.rpc.protocol.codec;

import com.netty.rpc.protocol.protocol.MessageHeader;
import com.netty.rpc.protocol.protocol.RpcProtocol;
import com.netty.rpc.protocol.serialization.RpcSerialization;
import com.netty.rpc.protocol.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    /*
   +---------------------------------------------------------------+
   | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
   +---------------------------------------------------------------+
   | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
   +---------------------------------------------------------------+
   |                   数据内容 （长度不定）                          |
   +---------------------------------------------------------------+
   */
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        MessageHeader header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getSerialization());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getRequestId());
        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(msg.getBody());
        out.writeInt(data.length);
        out.writeBytes(data);


    }
}
