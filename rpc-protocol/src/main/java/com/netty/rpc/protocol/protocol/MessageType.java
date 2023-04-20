package com.netty.rpc.protocol.protocol;

import lombok.Getter;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public enum MessageType {

    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);

    @Getter
    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public static MessageType findType(int type) {
        for (MessageType msgType : MessageType.values()) {
            if (msgType.type == type) {
                return msgType;
            }
        }
        return null;
    }
}
