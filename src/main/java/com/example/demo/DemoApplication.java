package com.example.demo;

import com.example.demo.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args)throws Exception {
        SpringApplication.run(DemoApplication.class, args);
        NettyServer nettyServer = new NettyServer();
        nettyServer.init();
    }



}
