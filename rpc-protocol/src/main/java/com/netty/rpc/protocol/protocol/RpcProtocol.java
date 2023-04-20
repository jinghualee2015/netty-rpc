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
public class RpcProtocol<T> implements Serializable {
    private MessageHeader header;
    private T body;
}
