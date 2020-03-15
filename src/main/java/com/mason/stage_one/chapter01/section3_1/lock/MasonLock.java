package com.mason.stage_one.chapter01.section3_1.lock;

import com.mason.stage_one.chapter01.section3_1.aqs.MasonAqs;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by WM on 2020/3/10
 */
public class MasonLock implements Lock {

    // 抽象工具类AQS
    private MasonAqs aqs = new MasonAqs() {
        @Override
        public boolean tryAcquire() {
            return owner.compareAndSet(null, Thread.currentThread());
        }

        @Override
        public boolean tryRelease() {
            // 可冲入的情况下，要判断资源的占用情况
            // state字段保存了资源的占用次数
            return owner.compareAndSet(Thread.currentThread(), null);
        }
    };

    @Override
    public void lock() {
        aqs.acquire();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return aqs.tryAcquire();
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        aqs.release();
    }

    @NotNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
