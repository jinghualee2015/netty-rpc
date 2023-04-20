package com.netty.rpc.registry;

import com.netty.rpc.core.ServiceMeta;

import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class EurekaRegistryService implements RegistryService {
    public EurekaRegistryService(String registryAddr) {

    }

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
