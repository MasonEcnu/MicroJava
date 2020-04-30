package com.mason;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by WM on 2020/4/14
 */
public interface Constants {

    // 默认地址
    String SERVER_HOST = "localhost";

    // 默认端口
    int SERVER_PORT = 10101;

    // 默认编码格式
    Charset SERVER_CHARSET = StandardCharsets.UTF_8;

    // buffer大小
    int SERVER_BUFFER_SIZE = 256;

    // byte大小
    int SERVER_BYTE_SIZE = 1024;

}
