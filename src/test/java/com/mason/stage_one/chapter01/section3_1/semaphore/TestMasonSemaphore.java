package com.mason.stage_one.chapter01.section3_1.semaphore;

import org.junit.Test;

import java.util.Random;

/**
 * Created by WM on 2020/3/11
 */
public class TestMasonSemaphore {

    // junit无法测试多线程的程序
    // 会自动调用System.exit(0)
    @Test
    public void testMasonSemaphore() throws InterruptedException {
        TestMasonSemaphore demo = new TestMasonSemaphore();
        // 限制请求数量
        MasonSemaphore semaphore = new MasonSemaphore(5);
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
        Thread.sleep(100000L);
    }

    // 限流，5个线程同时访问
    private void service(String vip) throws InterruptedException {
        System.out.println("楼上出来迎接贵宾一位，贵宾编号：" + vip + "......");
        Thread.sleep(new Random().nextInt(3000));
        System.out.println("欢送贵宾出门，贵宾编号：" + vip);
    }
}