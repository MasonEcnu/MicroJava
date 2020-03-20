package com.mason.stage_one.chapter01.section3_1.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mwu on 2020/3/20
 */
public class TestReentrantLock {

    private int i = 0;

    private void add() {
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        TestReentrantLock test = new TestReentrantLock();
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                test.add();
            } finally {
                lock.unlock();

            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
                test.add();
            } finally {
                lock.unlock();

            }
        }, "t2");
        t1.start();

        Thread.sleep(1000L);
        t2.start();
        Thread.sleep(1000L);
        System.out.println(test.i);
    }
}
