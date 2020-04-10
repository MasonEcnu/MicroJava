package com.mason.stage_one.chapter_one.section_two.atom;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by WM on 2020/4/2
 */
public class LongAdderDemo {
    private long count = 0;

    // 同步代码块
    public void testSync() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000L) {
                    synchronized (this) {
                        ++count;
                    }
                }
                long endTime = System.currentTimeMillis();
                System.out.format("SyncThread spend:%s ms, final value:%s\n", endTime - startTime, count);
            }).start();
        }
    }

    private AtomicLong atomicLong = new AtomicLong();

    public void testAtomicLong() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000L) {
                    atomicLong.incrementAndGet();
                }
                long endTime = System.currentTimeMillis();
                System.out.format("AtomicLong spend:%s ms, final value:%s\n", endTime - startTime, atomicLong.get());
            }).start();
        }
    }

    private LongAdder longAdder = new LongAdder();

    public void testLongAdder() {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000L) {
                    longAdder.increment();
                }
                long endTime = System.currentTimeMillis();
                System.out.format("LongAdder spend:%s ms, final value:%s\n", endTime - startTime, longAdder.sum());
            }).start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testSync();
        Thread.sleep(2000L);
        demo.testAtomicLong();
        Thread.sleep(2000L);
        demo.testLongAdder();
    }
}
