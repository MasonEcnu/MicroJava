package com.mason.stage_one.chapter_one.section_three;

import com.mason.stage_one.chapter_one.section_three.source.ReentrantReadWriteLock;

/**
 * Created by WM on 2020/4/4
 */
public class TestReadWriteLock {

    private volatile int value = 0;


    private int readValue() {
        return value;
    }

    private void writeValue(int temp) {
        value = temp;
    }

    public static void main(String[] args) throws InterruptedException {
        TestReadWriteLock demo = new TestReadWriteLock();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        for (int i = 0; i < 10; i++) {
            // 读线程
            int finalI = i;
            new Thread(() -> {
                try {
                    readLock.lock();
                    int value = demo.readValue();
                    System.out.format("读线程-%s, value:%s\n", finalI, value);
                } finally {
                    readLock.unlock();
                }
            }).start();

            // 写线程
            new Thread(() -> {
                try {
                    writeLock.lock();
                    int value = demo.readValue();
                    demo.writeValue(value + 1);
                    value = demo.readValue();
                    System.out.format("写线程-%s, value:%s\n", finalI, value);
                } finally {
                    writeLock.unlock();
                }
            }).start();
        }

        Thread.sleep(2000L);
        System.out.println("Main线程执行完毕。。。");
    }
}
