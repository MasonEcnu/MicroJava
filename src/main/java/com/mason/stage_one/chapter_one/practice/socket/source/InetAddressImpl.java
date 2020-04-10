package com.mason.stage_one.chapter_one.practice.socket.source;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by mwu on 2020/4/9
 */
interface InetAddressImpl {

    String getLocalHostName() throws UnknownHostException;

    InetAddress[]
    lookupAllHostAddr(String hostname) throws UnknownHostException;

    String getHostByAddr(byte[] addr) throws UnknownHostException;

    InetAddress anyLocalAddress();

    InetAddress loopbackAddress();

    boolean isReachable(InetAddress addr, int timeout, NetworkInterface netif, int ttl) throws IOException;
}
