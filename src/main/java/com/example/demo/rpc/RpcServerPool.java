package com.example.demo.rpc;

import com.example.demo.dto.RpcServerDto;
import com.example.demo.netty.NettyClient;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pikaqiu
 */
@Component
@Slf4j
public class RpcServerPool {

    private final Map<String, RpcServerDto> serverDtoMap = new HashMap<>();

    private Map<String, List<NettyClient>> channelMap = new HashMap<>();

    /**
     * 初始化所有连接
     */
//    @PostConstruct
    public void initAllConnect() {

        RpcServerBuild rpcServerPoolBuild = new RpcServerBuild();
        rpcServerPoolBuild.serverAdd("user", "127.0.0.1", 7001)
                .serverAdd("user", "127.0.0.1", 7002);

        for (String serverName : serverDtoMap.keySet()) {
            RpcServerDto rpcServerDto = serverDtoMap.get(serverName);
            for (RpcServerDto.Example example : rpcServerDto.getExamples()) {
                //循环创建连接
                log.info("创建连接 服务: {}：ip: {} ,port: {}", serverName, example.getIp(), example.getPort());

                NettyClient nettyClient = new NettyClient();
                nettyClient.initClient().createConnect(5, example.getIp(), example.getPort());

                List<NettyClient> nettyClients = channelMap.get(serverName);

                if (nettyClients == null) {
                    nettyClients = new ArrayList<>();
                    channelMap.put(serverName, nettyClients);
                }
                nettyClients.add(nettyClient);
            }
        }
    }

    /**
     * 获取一个连接
     *
     * @return
     */
    public ChannelFuture getChannelByServerName(String serverName) {
        //随机获取一个连接
        List<NettyClient> nettyClients = channelMap.get(serverName);
        return nettyClients.get((int) (Math.random() * (nettyClients.size()))).getChannelFuture();
    }

    public class RpcServerBuild {

        public RpcServerBuild serverAdd(String serverName, String ip, int port) {
            RpcServerDto serverDto = serverDtoMap.get(serverName);

            if (serverDto == null) {
                serverDto = new RpcServerDto(serverName);
            }
            serverDto.addExample(ip, port);
            serverDtoMap.put(serverName, serverDto);

            return this;
        }

    }

}
