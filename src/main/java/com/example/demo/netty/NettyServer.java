package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @program: demo
 * @description:
 * @author: xiaoye
 * @create: 2019-08-12 11:29
 **/
public class NettyServer {

    EventLoopGroup pGroup = new NioEventLoopGroup();
    EventLoopGroup cGroup = new NioEventLoopGroup();

    /**
     * 初始化服务端
     * @throws Exception
     */
    public void init()throws Exception{
        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup, cGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置日志
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new IdleStateHandler(0, 0, 60));
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
//                        sc.pipeline().addLast(new JsonObjectDecoder());
                        sc.pipeline().addLast(new ServerHeartBeatHandler());
                    }
                });
        ChannelFuture cf = b.bind(7001).sync();

//        ChannelFuture cf1 = b.bind(7002).sync();

        cf.channel().closeFuture().sync();

//        cf1.channel().closeFuture().sync();

    }


    /**
     * 关闭连接
     */
    public void shotDown(){
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }
}
