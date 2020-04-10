package com.mason.stage_one.chapter_one.practice.socket.communicate.server;

import com.mason.stage_one.chapter_one.practice.socket.communicate.tools.SocketTools;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
public class TcpSocketServer {

    /**
     * 服务器线程池
     * 1.让服务器端的任务处理异步
     * 2.管理同时处理的请求数量
     */
    private static final ThreadPoolExecutor collectPool =
            new ThreadPoolExecutor(4, 4, 365L, TimeUnit.DAYS, new LinkedBlockingDeque<>(1));

    private static final int PORT = 10101;

    public static void main(String[] args) {
        System.out.println("。。。。。。TCP Server开始启动。。。。。。");
        startServer();
    }

    /**
     * 启动服务器
     */
    private static void startServer() {
        try {
            // 初始化服务器
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            // 绑定地址
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("。。。。。。TCP Server启动成功。。。。。。");

            // 自旋监听客户端连接
            while (true) {
                Socket socket = serverSocket.accept();

                // 如果队列中有数据了
                // 说明服务端已经到了并发处理的极限了
                // 此时需要返回客户端有意义的信息
                if (collectPool.getQueue().size() >= 1) {
                    System.out.println("达到服务器处理极限。。。拒绝客户端请求");
                    rejectRequest(socket);
                    continue;
                }

                try {
                    // 异步处理客户端提交上来的任务
                    collectPool.submit(new ServerSocketService(socket));
                } catch (Exception e) {
                    socket.close();
                    throw new RuntimeException("TCP Server 提交任务异常。。。");
                }

            }
        } catch (Throwable e) {
            System.out.println("。。。。。。TCP Server启动失败。。。。。。");
            e.printStackTrace();
        }

    }

    /**
     * 拒绝请求
     *
     * @param socket 客户端socket
     */
    private static void rejectRequest(Socket socket) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            byte[] bytes = "服务器太忙了，请稍后重试~".getBytes(Charset.forName("UTF-8"));
            SocketTools.segmentWrite(bytes, outputStream);
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                SocketTools.close(socket, outputStream, null, null, null);
            } catch (Exception e) {
                System.out.println("Server socket关闭失败。。。。。。");
            }
        }
    }
}
