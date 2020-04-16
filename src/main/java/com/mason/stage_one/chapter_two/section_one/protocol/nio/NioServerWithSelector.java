package com.mason.stage_one.chapter_two.section_one.protocol.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.mason.Constants.SERVER_CHARSET;
import static com.mason.Constants.SERVER_PORT;

/**
 * Created by WM on 2020/4/12
 * Selector处理多客户端连接
 * 事件驱动机制
 */
public class NioServerWithSelector {

    public static void main(String[] args) throws Exception {
        // 创建网络服务端
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverChannel.configureBlocking(false);
        // 绑定端口
        serverChannel.bind(new InetSocketAddress(SERVER_PORT));

        // 创建一个选择器，并将channel注册上去
        Selector selector = Selector.open();
        SelectionKey selectionKey = serverChannel.register(selector, 0, serverChannel);
        // 注册监听事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动成功，端口：" + SERVER_PORT);

        while (true) {
            // 改用select方式轮询
            selector.select();
            // 获取事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历查询结果
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 被封装的查询结果
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    // 关注read和write两个事件
                    ServerSocketChannel server = (ServerSocketChannel) key.attachment();
                    // 将拿到的客户端连接通道
                    // 注册到selector上
                    SocketChannel clientChannel = server.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ, clientChannel);
                    System.out.println("收到新连接：" + clientChannel.getRemoteAddress());
                }

                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.attachment();
                    try {
                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                        if (socketChannel.read(requestBuffer) == 0) {
                            // 等于0,代表这个通道没有数据需要处理,那就待会再处理
                            continue;
                        }
                        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
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
                        System.out.println("来自：" + socketChannel.getRemoteAddress());
                        // 响应结果
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes(SERVER_CHARSET));
                        while (buffer.hasRemaining()) {
                            // 非阻塞
                            socketChannel.write(buffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 取消事件订阅
                        key.cancel();
                    }
                }
            }
            selector.selectNow();
        }
    }
}
