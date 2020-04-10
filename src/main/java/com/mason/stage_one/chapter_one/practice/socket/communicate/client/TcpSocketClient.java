package com.mason.stage_one.chapter_one.practice.socket.communicate.client;

import com.mason.stage_one.chapter_one.practice.socket.communicate.tools.SocketTools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by mwu on 2020/4/10
 * Socket通信demo
 * 客户端
 */
public class TcpSocketClient {

    private static final ThreadPoolExecutor socketPool =
            new ThreadPoolExecutor(50, 50, 365L, TimeUnit.DAYS, new LinkedBlockingDeque<>(400));
    private static final int MSG_NUM = 10;

    private static final String DOMAIN_NAME = "localhost";
    private static final int PORT = 10101;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("。。。。。。客户端开始运行。。。。。。");
        // 模拟客户端同时向服务器发送6条信息
        for (int i = 0; i < MSG_NUM; i++) {
            int finalI = i;
            socketPool.submit(() -> {
                send("Hello World:" + finalI);
            });
        }
        Thread.sleep(1000000000L);
        System.out.println("。。。。。。客户端运行结束。。。。。。");
    }

    /**
     * 发送tcp消息
     *
     * @param content 发送内容
     * @return result
     */
    private static String send(String content) {
        Socket socket = null;
        OutputStream outputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        InputStream is = null;
        StringBuffer response;
        try {
            // 初始化socket
            socket = new Socket();
            socket.setReuseAddress(true);

            // 连接服务器，超时设置10000毫秒
            socket.connect(new InetSocketAddress(DOMAIN_NAME, PORT), 10000);
            System.out.println("TCP Client成功和服务器建立连接。。。");

            // 准备发送消息给服务器
            outputStream = socket.getOutputStream();
            // 设置编码
            byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
            //输出字节码
            SocketTools.segmentWrite(bytes, outputStream);
            // 关闭输出
            socket.shutdownOutput();
            System.out.println("TCP Client发送消息成功：" + content);

            // 等待服务器返回
            socket.setSoTimeout(50000);// 设置50秒超时

            // 得到服务器端返回流
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            // 从流中读取返回值
            response = SocketTools.segmentRead(br);
            // 关闭输入流
            socket.shutdownInput();
            // 关闭各种流和套接字
            SocketTools.close(socket, outputStream, isr, br, is);

            System.out.println("TCP Client收到服务器回复的消息：" + response.toString());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Client socket连接失败");
        } finally {
            try {
                SocketTools.close(socket, outputStream, isr, br, is);
            } catch (Exception e) {
                System.out.println("Client socket关闭失败。。。。。。");
            }
        }
    }
}
