package com.netty.rpc.core;

import lombok.Data;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Data
public class ServiceMeta {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;
}
