package com.example.demo.com;

import com.example.demo.rpc.RpcFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * @author pikaqiu
 */
@Slf4j
@Component
public class ProxyFactory {
    /**
     * 获取接口代理
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public <T> T getInterfaceInfo(Class<T> interfaceClass) {
        Class[] interfaceClassArray = new Class[]{interfaceClass};
        T server = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaceClassArray, new RpcFactory(interfaceClass));
        return server;
    }
}
