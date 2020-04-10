package com.mason.stage_one.chapter_one.practice.socket.source;

import java.net.UnknownHostException;

/**
 * Created by mwu on 2020/4/9
 */
public interface NameService {
    InetAddress[] lookupAllHostAddr(String var1) throws UnknownHostException;

    String getHostByAddr(byte[] var1) throws UnknownHostException;
}
