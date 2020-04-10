package com.mason.stage_one.chapter_one.section_three;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by WM on 2020/3/9
 */

// 实现一把独占锁
public class MasonExclusiveLock implements Lock {
    // 1.如何判断一个锁的状态或者说拥有者
    private volatile AtomicReference<Thread> owner = new AtomicReference<>();

    // 保存正在等待的线程
    private volatile LinkedBlockingDeque<Thread> waiters = new LinkedBlockingDeque<>();

    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        boolean addQ = true;
        while (!tryLock()) {
            if (addQ) {
                // 没拿到锁，加入到等待集合
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                // 挂起当前线程
                LockSupport.park(); // 伪唤醒，非unpark唤醒
            }
        }
        // 将线程从等待集合移除
        waiters.remove(Thread.currentThread());
    }

    @Override
    public void unlock() {
        // 释放owner
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            // 释放成功，通知等待者
            waiters.forEach(LockSupport::unpark);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @NotNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
