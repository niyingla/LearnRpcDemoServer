package com.example.demo.util;

import com.example.demo.dto.RpcRequestDto;
import com.example.demo.netty.FutureResult;
import io.netty.channel.ChannelFuture;

/**
 * @program: demo
 * @description:
 * @author: xiaoye
 * @create: 2019-08-12 15:02
 **/
public class ChannelUtils {

    /**
     * 发送远程请求
     *
     * @param channel
     * @param rpcRequestDto
     * @return
     */
    public static Object sendChannelRpcRequest(ChannelFuture channel, RpcRequestDto rpcRequestDto) {
        channel.channel().writeAndFlush(rpcRequestDto);
        //等待结果
       return FutureResult.getResult(rpcRequestDto.getRequestId());
    }


}
