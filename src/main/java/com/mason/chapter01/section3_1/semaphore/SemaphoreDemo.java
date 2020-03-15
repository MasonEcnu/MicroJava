package com.mason.chapter01.section3_1.semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by WM on 2020/3/11
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        SemaphoreDemo demo = new SemaphoreDemo();
        // 限制请求数量
        Semaphore semaphore = new Semaphore(5);
        int N = 10;
        for (int i = 0; i < N; i++) {
            String vipNo = "Vip-00" + i;
            new Thread(() -> {
                try {
                    semaphore.acquire();    // 获取令牌
                    demo.service(vipNo);
                    semaphore.release();    // 释放令牌
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // 限流，5个线程同时访问
    private void service(String vip) throws InterruptedException {
        System.out.println("楼上出来迎接贵宾一位，贵宾编号：" + vip + "......");
        Thread.sleep(new Random().nextInt(3000));
        System.out.println("欢送贵宾出门，贵宾编号：" + vip);
    }
}
