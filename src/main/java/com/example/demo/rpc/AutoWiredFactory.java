package com.example.demo.rpc;

import com.example.demo.annotation.RpcServerCase;
import com.example.demo.com.ProxyFactory;
import com.example.demo.util.ScannerUtils;
import com.example.demo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author pikaqiu
 */
@Component
public class AutoWiredFactory implements ApplicationListener<ApplicationStartedEvent> {

    /**
     * 需要加載实例列表
     */
    private List<Class> rpcInterFace = ScannerUtils.getAnnotations(RpcServerCase.class, "com.example.demo");

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    public void setBean(Class interfaceServer) {
        ProxyFactory proxyFactory = new ProxyFactory();
        Object interfaceInfo = proxyFactory.getInterfaceInfo(interfaceServer);
        defaultListableBeanFactory.registerSingleton(StringUtils.lowerFirst(interfaceServer.getSimpleName()), interfaceInfo);
    }



    /**
     * 通过扫描获取所有rpc代理类
     */
    public void autoWiredRpcProxy() {
        for (Class inter : rpcInterFace) {
            setBean(inter);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationEvent) {
        autoWiredRpcProxy();
    }
}
