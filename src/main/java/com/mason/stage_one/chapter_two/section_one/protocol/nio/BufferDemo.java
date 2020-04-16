package com.mason.stage_one.chapter_two.section_one.protocol.nio;

import java.nio.ByteBuffer;

/**
 * Created by WM on 2020/4/12
 */
public class BufferDemo {

    public static void main(String[] args) {
        // 构建一个byte字节缓冲区
        // 容量为4
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
//        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        // 默认写入模式，查看3个重要指标
        System.out.format("ByteBuffer初始化，容量capacity:%s，位置position：%s，限制limit：%s\n", byteBuffer.capacity(), byteBuffer.position(), byteBuffer.limit());
        // 写入3个字节数据
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);
        System.out.format("写入3字节数据后，容量capacity:%s，位置position：%s，限制limit：%s\n", byteBuffer.capacity(), byteBuffer.position(), byteBuffer.limit());
        // 转换为读取模式
        // 不调用flip也是可以读取数据的
        // 但是position记录的读取位置不对
        System.out.println("#####开始读取数据#####");
        byteBuffer.flip();
        byte a = byteBuffer.get();
        System.out.println(a);
        byte b = byteBuffer.get();
        System.out.println(b);
        System.out.format("读取2字节数据后，容量capacity:%s，位置position：%s，限制limit：%s\n", byteBuffer.capacity(), byteBuffer.position(), byteBuffer.limit());

        // 继续写入3字节
        // 此时读模式下，limit=3，position=2
        // 继续写入只能覆盖写入一条数据
        // clear()方法清除整个缓冲区
        // compact()方法仅清除已读缓冲区，转为写入模式
        byteBuffer.compact();
        byteBuffer.put((byte) 4);
        byteBuffer.put((byte) 5);
        byteBuffer.put((byte) 6);
        System.out.format("最终情况，容量capacity:%s，位置position：%s，限制limit：%s\n", byteBuffer.capacity(), byteBuffer.position(), byteBuffer.limit());
        byteBuffer.flip();
        System.out.println(byteBuffer.toString());
        byteBuffer.rewind();    // 重置position为0
        byteBuffer.mark();  // 标记position的位置
        byteBuffer.reset(); // 重置position为上次mark的位置
    }
}
