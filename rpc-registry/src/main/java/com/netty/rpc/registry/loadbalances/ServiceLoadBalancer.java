package com.netty.rpc.registry.loadbalances;

import java.util.List;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public interface ServiceLoadBalancer<T> {
    T select(List<T> servers, int hashCode);
}
