package com.netty.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Data
public class RpcResponse implements Serializable {
    private Object data;

    private String message;
}
