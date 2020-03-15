package com.mason.chapter01.section3_1.cyclic;

import java.util.Queue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by WM on 2020/3/12
 */
// 循环屏障，示例：数据库批量插入
// 游戏大厅...多人组队打副本
public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {
        Queue<String> sqls = new LinkedBlockingDeque<>();
        int N = 4;
        // 每当有N个线程处于await状态时，则会触发barrierAction的执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N, () -> {
            // 这里每满足四次数据库操作，则触发一个批量执行
            System.out.println("有" + N + "个线程执行了：" + Thread.currentThread());
            for (int i = 0; i < N; i++) {
                System.out.println(sqls.poll());
            }
        });

        for (int i = 0; i < 12; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    // 缓存起来
                    sqls.add("data - " + finalI + "-" + Thread.currentThread());
                    // 模拟操作
                    Thread.sleep(2000L);
                    // 等待屏障打开，只有当四个线程都执行到这段代码时
                    // 才会继续向下执行
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread() + "插入完毕...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(2000L);
    }
}
