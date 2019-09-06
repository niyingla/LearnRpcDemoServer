package com.example.demo.rpc;

import com.example.demo.annotation.RpcServerCase;
import com.example.demo.dto.RpcRequestDto;
import com.example.demo.util.ChannelUtils;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 远程调用服务客户端类
 *
 * @author pikaqiu
 */
@Slf4j
@Component
public class RpcClient {

    @Autowired
    private static RpcServerPool rpcServerPool;

    public static Object sendRpcRequest(String classPath, Class interfaceClass, String method, Object[] args) {
        //参数对象转换成能字节  远程调用
        RpcServerCase rpcServerCase = (RpcServerCase) interfaceClass.getAnnotation(RpcServerCase.class);
        if (rpcServerCase != null) {
            log.info(rpcServerCase.serverName());
        }
        RpcRequestDto rpcRequestDto = new RpcRequestDto(System.currentTimeMillis() + "", classPath, method, args, null);
        ChannelFuture channel = rpcServerPool.getChannelByServerName(rpcServerCase.serverName());

        return ChannelUtils.sendChannelRpcRequest(channel, rpcRequestDto);
    }

}

