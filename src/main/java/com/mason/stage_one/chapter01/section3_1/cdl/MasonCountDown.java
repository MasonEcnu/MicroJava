package com.mason.stage_one.chapter01.section3_1.cdl;

import com.mason.stage_one.chapter01.section3_1.aqs.MasonAqs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WM on 2020/3/12
 */
public class MasonCountDown {

    private MasonAqs aqs = new MasonAqs() {
        // 如果非等于0，代表当前还有线程没有准备就绪，则任务需要等待
        @Override
        public int tryAcquireShared() {
            return aqs.getState().get() == 0 ? 1 : -1;
        }

        // 如果非等于0，表示还有线程没有准备就绪，则不会通知执行
        @Override
        public boolean tryReleaseShared() {
            return aqs.getState().decrementAndGet() == 0;
        }
    };

    public void await() {
        aqs.acquireShared();
    }

    public void countDown() {
        aqs.releaseShared();
    }

    public MasonCountDown(int count) {
        aqs.setState(new AtomicInteger(count));
    }

    public static void main(String[] args) {
        // 创建一个计数器
        // 一个请求需要调用多个接口
        MasonCountDown downLatch = new MasonCountDown(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 参与者只参与计数
                // 不影响后续的代码执行
                downLatch.countDown();
                System.out.println("我是" + Thread.currentThread() + "，我执行了接口" + finalI + "的调用");
            }).start();
        }

        // 线程阻塞在这里，知道downLatch倒计时为0
        // 即就是countDown为0
        // 某个线程可用于等待多个线程的执行完毕
        downLatch.await();
        System.out.println("嘿哈！");
    }
}
