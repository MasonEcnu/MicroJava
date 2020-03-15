package com.mason.chapter01.section3_1.semaphore;

import com.mason.chapter01.section3_1.aqs.MasonAqs;

/**
 * Created by WM on 2020/3/11
 */
public class MasonSemaphore extends MasonAqs {
    // 抽象工具类AQS
    private MasonAqs aqs = new MasonAqs() {
        // 获取-1
        @Override
        public int tryAcquireShared() {
            while (true) {
                int permits = getState().get();
                int n = permits - 1;
                if (permits <= 0 || n < 0) {
                    return -1;
                }
                if (getState().compareAndSet(permits, n)) {
                    return 1;
                }
            }
        }

        // 释放+1
        @Override
        public boolean tryReleaseShared() {
            return getState().incrementAndGet() >= 0;
        }
    };

    // 传入令牌数量
    public MasonSemaphore(int permits) {
        aqs.getState().set(permits);
    }

    // 获取令牌
    public void acquire() {
        aqs.acquireShared();
    }

    // 释放令牌
    public void release() {
        aqs.releaseShared();
    }
}
