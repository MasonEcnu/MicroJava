package com.mason.stage_one.chapter_two.section_one.protocol.bio;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.mason.Constants.*;

/**
 * Created by WM on 2020/4/12
 */
public class BioClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        OutputStream out = socket.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, SERVER_CHARSET);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();
        writer.write(msg);
        writer.flush();
        scanner.close();
        socket.close();
    }
}
