package com.mason.stage_one.chapter_two.section_one.practice.easy_demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.mason.Constants.*;

/**
 * Created by WM on 2020/4/26
 */
public class MasonSocketServer {

    public static void main(String[] args) throws IOException, Exception {
        // server
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        // 获取新连接
        while (true) {
            final Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            while (true) {
                byte[] request = new byte[SERVER_BYTE_SIZE];
                int read = inputStream.read(request);
                if (read == -1) {
                    break;
                }
                // 得到请求内容，解析，得到发送对象和发送内容
                String content = new String(request, SERVER_CHARSET);
                System.out.println(content);
            }
        }
    }
}
