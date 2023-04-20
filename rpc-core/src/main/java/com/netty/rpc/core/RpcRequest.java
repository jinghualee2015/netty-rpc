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
public class RpcRequest implements Serializable {
    private String serviceVersion;
    private String className;
    private String methodName;

    private Object[] params;
    private Class<?>[] parameterTypes;

}
