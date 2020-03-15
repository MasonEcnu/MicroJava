package com.mason.chapter01.section3_1.cdl;

import java.util.concurrent.CountDownLatch;

/**
 * Created by WM on 2020/3/12
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个计数器
        // 一个请求需要调用多个接口
        CountDownLatch downLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
                System.out.println("我是" + Thread.currentThread() + "，我执行了接口" + finalI + "的调用");
            }).start();
        }

        // 线程阻塞在这里，知道downLatch倒计时为0
        // 即就是countDown为0
        // 某个线程可用于等待多个线程的执行完毕
        downLatch.await();
        System.out.println("嘿哈！");

//        for (int i = 0; i < 9; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                System.out.println(Thread.currentThread() + "准备就绪...");
//                downLatch.countDown();
//                try {
//                    downLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("我是" + Thread.currentThread() + "，我执行了接口" + finalI + "的调用");
//            }).start();
//        }
//
//        // 等待两秒，最后的线程才启动
//        Thread.sleep(2000L);
//
//        new Thread(() -> {
//            downLatch.countDown();
//            try {
//                downLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("我是最后一个线程。。。");
//        }).start();
    }
}
