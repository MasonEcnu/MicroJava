package com.mason.stage_one.chapter_one.practice.socket.communicate.server;

import com.mason.stage_one.chapter_one.practice.socket.communicate.tools.SocketTools;
import com.mason.stage_one.chapter_one.section_one.thread_sealing.source.Thread;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by mwu on 2020/4/10
 */
public class ServerSocketService implements Runnable {

    private Socket socket;

    public ServerSocketService(Socket socket) {
        if (socket == null) {
            throw new RuntimeException("ServerSocketService socket is null");
        }
        this.socket = socket;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.format("Thread:%s 开始执行消息处理任务", currentThread.getName());

        OutputStream outputStream = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            // 接收消息
            socket.setSoTimeout(10000); // 10秒没有获取到数据就断开连接
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            StringBuffer sb = SocketTools.segmentRead(br);
            socket.shutdownInput();
            System.out.format("Thread:%s 收到消息：", sb.toString());

            // 模拟服务端耗时处理
            Thread.sleep(2000L);
            String response = sb.toString();
            // 返回处理结果给客户端，原封不动
            outputStream = socket.getOutputStream();
            byte[] bytes = response.getBytes(Charset.forName("UTF-8"));
            SocketTools.segmentWrite(bytes, outputStream);
            socket.shutdownOutput();

            // 关闭流
            SocketTools.close(socket, outputStream, isr, br, is);
            System.out.format("Thread:%s 执行任务完成", currentThread.getName());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                SocketTools.close(socket, outputStream, isr, br, is);
            } catch (Exception e) {
                System.out.println("Server socket关闭失败。。。。。。");
            }
        }
    }
}
