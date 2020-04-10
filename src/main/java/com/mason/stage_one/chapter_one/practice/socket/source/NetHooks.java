package com.mason.stage_one.chapter_one.practice.socket.source;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by mwu on 2020/4/9
 */
public final class NetHooks {
    public NetHooks() {
    }

    public static void beforeTcpBind(FileDescriptor var0, InetAddress var1, int var2) throws IOException {
    }

    public static void beforeTcpConnect(FileDescriptor var0, InetAddress var1, int var2) throws IOException {
    }
}
