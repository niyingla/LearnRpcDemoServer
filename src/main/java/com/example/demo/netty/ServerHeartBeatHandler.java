
package com.example.demo.netty;

import com.example.demo.dto.RpcRequestDto;
import com.example.demo.rpc.FrameWork;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接受远程请求 并响应结果
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcRequestDto){
            RpcRequestDto rpcRequestDto = (RpcRequestDto)msg;
            Object invoke = FrameWork.methodInvoke(rpcRequestDto.getClassPath(), rpcRequestDto.getMethodName(), rpcRequestDto.getArgs());
            rpcRequestDto.setResult(invoke);
            ctx.writeAndFlush(rpcRequestDto);
            ReferenceCountUtil.release(rpcRequestDto);
        } else {
            System.out.println(msg);
        }
    }
}
