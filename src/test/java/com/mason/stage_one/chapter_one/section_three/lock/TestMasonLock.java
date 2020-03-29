package com.mason.stage_one.chapter_one.section_three.lock;

import org.junit.Test;

/**
 * Created by WM on 2020/3/10
 */
public class TestMasonLock {

    private MasonLock lock = new MasonLock();
    private int i = 0;

    private void add() {
        try {
            lock.lock();
            i++;
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testMasonLock() throws InterruptedException {
        TestMasonLock test = new TestMasonLock();
        // 开两个线程对i进程++操作
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10086; j++) {
                    test.add();
                }
            }).start();
        }

        Thread.sleep(2000L);
        System.out.println(test.i);
    }
}
