package com.netty.rpc.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {
    private int servicePort;
    private String registryAddr;
    private String registryType;
}
