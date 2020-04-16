package com.mason.stage_one.chapter_two.section_one.protocol.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import static com.mason.Constants.SERVER_CHARSET;
import static com.mason.Constants.SERVER_PORT;

/**
 * Created by WM on 2020/4/12
 * 轮询处理多客户端连接
 */
public class NioServerWithPolling {

    private static ArrayList<SocketChannel> channels = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // 创建网络服务端
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverChannel.configureBlocking(false);
        // 绑定端口
        serverChannel.bind(new InetSocketAddress(SERVER_PORT));
        System.out.println("服务器启动成功，端口：" + SERVER_PORT);

        // 持续监听
        while (true) {
            // 获取新的TCP连接通道
            SocketChannel channel = serverChannel.accept();
            // TCP请求 读取/响应
            if (channel != null) {
                System.out.println("收到新连接：" + channel.getRemoteAddress());
                // 设置为非阻塞
                channel.configureBlocking(false);
                channels.add(channel);
            } else {
                // 没有连接的情况下，就轮询处理已收到的请求
                Iterator<SocketChannel> iterator = channels.iterator();
                if (iterator.hasNext()) {
                    SocketChannel clientChannel = iterator.next();
                    try {
                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                        if (clientChannel.read(requestBuffer) == 0) {
                            // 等于0,代表这个通道没有数据需要处理,那就待会再处理
                            continue;
                        }
                        while (clientChannel.isOpen() && clientChannel.read(requestBuffer) != -1) {
                            // 长连接的情况下
                            // 需要手动判断数据有没有读取结束
                            // 此处做一个简单判断：超过0字节就认为请求结束了
                            if (requestBuffer.position() > 0) break;
                        }
                        if (requestBuffer.position() == 0) continue;
                        requestBuffer.flip();
                        byte[] content = new byte[requestBuffer.limit()];
                        requestBuffer.get(content);
                        System.out.println("收到数据：" + new String(content, SERVER_CHARSET));
                        System.out.println("来自：" + clientChannel.getRemoteAddress());
                        // 响应结果
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes(SERVER_CHARSET));
                        while (buffer.hasRemaining()) {
                            // 非阻塞
                            clientChannel.write(buffer);
                        }
                        iterator.remove();
                    } catch (IOException e) {
                        e.printStackTrace();
                        iterator.remove();
                    }
                }
            }
        }
    }
}
