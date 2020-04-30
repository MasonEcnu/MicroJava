package com.mason.stage_one.chapter_two.section_one.practice.netty_demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.mason.Constants.SERVER_PORT;

/**
 * Created by WM on 2020/4/26
 * 支持解决粘包和拆包问题的Netty服务端
 */
public class MasonNettyServer {

    public static void main(String[] args) throws Exception {
        // 1、 线程定义
        // accept 处理连接的线程池，bossGroup
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        // read io 处理数据的线程池，workerGroup
        EventLoopGroup readGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(acceptGroup, readGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 3、 职责链定义（请求收到后怎么处理）
                            ChannelPipeline pipeline = ch.pipeline();
                            // TODO 3.1 增加解码器
                            pipeline.addLast(new MasonDecoder());
                            // TODO 3.2 打印出内容 handdler
                            pipeline.addLast(new MasonHandler());
                        }
                    });
            // 4、 绑定端口
            int port = SERVER_PORT;
            System.out.println("启动成功，端口：" + port);
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } finally {
            acceptGroup.shutdownGracefully();
            readGroup.shutdownGracefully();
        }
    }
}
