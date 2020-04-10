package com.mason.stage_one.chapter_one.section_three.concurrent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by WM on 2020/4/8
 */
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // 构造时需要指定容量
        // 默认非公平，有锁，ReentrantLock
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        new Thread(() -> {
            while (true) {
                try {
                    // poll：队列为空时，返回null
                    System.out.println("取到数据：" + queue.poll());
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(3000L);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // put阻塞，如果当前队列已经满了，则阻塞
                    queue.put(Thread.currentThread().getName());
                    System.out.println(Thread.currentThread() + "塞入数据完成！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }
}
