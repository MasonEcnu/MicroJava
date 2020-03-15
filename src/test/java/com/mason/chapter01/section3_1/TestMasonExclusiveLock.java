package com.mason.chapter01.section3_1;

import org.junit.Test;

/**
 * Created by WM on 2020/3/9
 */
public class TestMasonExclusiveLock {
    private MasonExclusiveLock lock = new MasonExclusiveLock();
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
    public void testMasonExclusiveLock() throws InterruptedException{
        TestMasonExclusiveLock test = new TestMasonExclusiveLock();
        // 开两个线程对i进程++操作
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    test.add();
                }
            }).start();
        }

        Thread.sleep(2000L);
        System.out.println(test.i);
    }
}
