package com.mason.stage_one.chapter_two.section_one.practice.easy_demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import static com.mason.Constants.*;

/**
 * Created by WM on 2020/4/26
 */
public class MasonSocketClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        OutputStream outputStream = socket.getOutputStream();

        // 消息长度固定为 220字节，包含有
        // 1. 目标用户ID长度为10， 10 000 000 000 ~ 19 999 999 999
        // 2. 消息内容字符串长度最多70。 按一个汉字3字节，内容的最大长度为210字节
        byte[] request = new byte[220];
        byte[] userId = "10000000000".getBytes(SERVER_CHARSET);
        byte[] content = "我爱你tony你爱我吗我爱你tony你爱我吗我爱你tony你爱我吗我爱你tony你爱我吗".getBytes(SERVER_CHARSET);
        System.arraycopy(userId, 0, request, 0, 10);
        System.arraycopy(content, 0, request, 10, content.length);

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    outputStream.write(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        Thread.sleep(2000L); // 两秒后退出
        socket.close();
    }
}
