package com.example.demo.rpc;

import com.example.demo.com.ProxyFactory;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author pikaqiu
 */
@Component
public class AutoWiredFactory {

    @Autowired
    private ProxyFactory proxyFactory;

    /**
     * 需要加載实例列表
     */
    private List<Class> rpcInterFace = Arrays.asList(UserInfoService.class);

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;


    public void setBean(Class interfaceServer) {
        Object interfaceInfo = proxyFactory.getInterfaceInfo(interfaceServer);
        defaultListableBeanFactory.registerSingleton(StringUtils.lowerFirst(interfaceServer.getSimpleName()), interfaceInfo);
    }


    /**
     * todo 通过扫描获取所有rpc代理类
     */
    @PostConstruct
    public void autoWiredRpcProxy() {
        for (Class inter : rpcInterFace) {
            setBean(inter);
        }
    }
}
