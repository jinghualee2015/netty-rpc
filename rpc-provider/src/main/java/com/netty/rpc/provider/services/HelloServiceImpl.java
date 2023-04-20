package com.netty.rpc.provider.services;

import com.netty.rpc.provider.annotation.RpcService;
import com.netty.rpc.serivce.api.HelloService;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@RpcService(serviceInterface = HelloService.class, serviceVersion = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
