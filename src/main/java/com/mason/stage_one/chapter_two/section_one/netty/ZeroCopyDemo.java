package com.mason.stage_one.chapter_two.section_one.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import static com.mason.Constants.SERVER_CHARSET;

/**
 * Created by WM on 2020/4/25
 * Netty零拷贝机制
 */
public class ZeroCopyDemo {

    public static void main(String[] args) {
        // wrap
        wrap();
        // slice
        slice();
        // composite
        composite();
    }

    private static void composite() {
        System.out.println("测试Netty复合ByteBuf--composite");
        ByteBuf buf1 = Unpooled.buffer(3);
        buf1.writeByte(1);
        ByteBuf buf2 = Unpooled.buffer(3);
        buf2.writeByte(4);
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        CompositeByteBuf newBuffer = compositeByteBuf.addComponents(true, buf1, buf2);
        System.out.println(newBuffer.toString());
    }

    private static void slice() {
        System.out.println("测试Netty分片ByteBuf--slice");
        ByteBuf buf = Unpooled.wrappedBuffer("hello".getBytes(SERVER_CHARSET));
        ByteBuf newBuffer = buf.slice(1, 2);
        newBuffer.unwrap();
        System.out.println(newBuffer.toString());
    }

    private static void wrap() {
        System.out.println("测试Netty包装ByteBuf--wrap");
        byte[] arr = {1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.wrappedBuffer(arr);
        System.out.println(buf.getByte(4));
        arr[4] = 6;
        System.out.println(buf.getByte(4));
    }
}
