package com.mason.stage_one.chapter_two.section_one.protocol.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static com.mason.Constants.*;

/**
 * Created by WM on 2020/4/12
 */
public class NioClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        while (!socketChannel.finishConnect()) {
            // 没连接上，则一直等待
            Thread.yield();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(SERVER_CHARSET));
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }

        // 读取响应
        System.out.println("收到服务器响应：");
        ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.isOpen() && socketChannel.read(responseBuffer) != -1) {
            if (responseBuffer.position() > 0) break;
        }

        responseBuffer.flip();
        byte[] content = new byte[responseBuffer.limit()];
        responseBuffer.get(content);
        System.out.println(new String(content, SERVER_CHARSET));
        scanner.close();
        socketChannel.close();
    }
}
