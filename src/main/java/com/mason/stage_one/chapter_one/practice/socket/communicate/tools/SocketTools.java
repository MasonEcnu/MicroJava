package com.mason.stage_one.chapter_one.practice.socket.communicate.tools;

import java.io.*;
import java.net.Socket;

/**
 * Created by mwu on 2020/4/10
 */
public class SocketTools {

    private static final int SIZE = 1024;

    /**
     * 关闭套接字和流
     *
     * @param socket       套接字
     * @param outputStream 输出流
     * @param isr          输入流读取
     * @param br           读缓缓冲
     * @param is           输入流
     */
    public static void close(Socket socket, OutputStream outputStream, InputStreamReader isr, BufferedReader br, InputStream is) {
        try {
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }

                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (isr != null) {
                isr.close();
            }

            if (br != null) {
                br.close();
            }

            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分段写
     *
     * @param bytes        字节信息
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    public static void segmentWrite(byte[] bytes, OutputStream outputStream) throws IOException {
        int len = bytes.length;
        int start, end = 0;
        for (int i = 0; end != bytes.length; i++) {
            start = i == 0 ? 0 : i * SIZE;
            end = len > SIZE ? start + SIZE : bytes.length;
            len -= SIZE;
            outputStream.write(bytes, start, end - start);
            outputStream.flush();
        }
    }

    /**
     * 分段读
     *
     * @param br 读缓冲区
     * @return StringBuffer 读到的内容
     * @throws IOException IO异常
     */
    public static StringBuffer segmentRead(BufferedReader br) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb;
    }
}
