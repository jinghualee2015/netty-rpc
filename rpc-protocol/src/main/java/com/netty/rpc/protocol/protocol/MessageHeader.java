package com.netty.rpc.protocol.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Data
public class MessageHeader implements Serializable {
    /**
     * +---------------------------------------------------------------------+
     * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte         |
     * +---------------------------------------------------------------------+
     * | 状态 1byte |       消息 ID 8 byte         | 消息长度 4 byte           |
     * +---------------------------------------------------------------------
     */

    private short magic;
    private byte version;
    private byte serialization;
    private byte msgType;
    private byte status;
    private long requestId;
    private int msgLen;
}
