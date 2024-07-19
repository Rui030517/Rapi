package com.yupi.springbootinit.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

public interface DemoService {
    String sayHello(String name);

    String sayHello2(String name);
    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }
}
