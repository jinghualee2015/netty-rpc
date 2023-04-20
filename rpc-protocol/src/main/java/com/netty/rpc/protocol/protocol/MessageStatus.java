package com.netty.rpc.protocol.protocol;

import lombok.Getter;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public enum MessageStatus {
    SUCCESS(0),
    FAIL(1);
    @Getter
    private final int code;

    MessageStatus(int code) {
        this.code = code;
    }


}
