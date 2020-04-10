package com.mason.stage_one.chapter_one.practice.socket.source;

/**
 * Created by mwu on 2020/4/9
 */
public interface NameServiceDescriptor {
    NameService createNameService() throws Exception;

    String getProviderName();

    String getType();
}
