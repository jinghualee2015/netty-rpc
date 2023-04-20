package com.netty.rpc.consumer.controller;

import com.netty.rpc.consumer.annotation.RpcReference;
import com.netty.rpc.serivce.api.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@RestController
public class HelloController {
    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @RpcReference(serviceVersion = "1.0.0", timeout = 3000)
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        return helloService.hello("netty rpc");
    }
}
