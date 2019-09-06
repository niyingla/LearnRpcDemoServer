package com.example.demo.com;

import com.example.demo.rpc.RpcClient;
import com.example.demo.util.Addresser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

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
    public  <T> T getInterfaceInfo(Class<T> interfaceClass) {

        Class[] interfaceClassArray = new Class[]{interfaceClass};

        T server = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),interfaceClassArray , new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args){
                //判断是否是接口自定义方法
                Method[] declaredMethods = interfaceClass.getDeclaredMethods();
                if (Arrays.asList(declaredMethods).indexOf(method) < 0) {
                    try {
                        return method.invoke(this, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return RpcClient.sendRpcRequest(method.getDeclaringClass().getPackage().getName(), interfaceClass, method.getName(), args);
            }
            @Override
            public boolean equals(Object obj) {
                return toString().equals(obj.toString());
            }

            @Override
            public String toString() {
                return "proxy$"+interfaceClass.getName() + "@" + Integer.toHexString(hashCode());
            }

            @Override
            public int hashCode() {
                Long address = Addresser.addressOf(this);
                return address.intValue();
            }

        });
        return server;
    }
}
