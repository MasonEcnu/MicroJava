package com.mason.stage_one.chapter01.section3_1.aqs;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by WM on 2020/3/10
 * 抽象队列同步器
 * state, owner, waiter(链表)
 */
public class MasonAqs {
    // acquire, acquireShared: 定义了资源争用的逻辑，如果没有拿到锁，则等待
    // tryAcquire, tryAcquireShared: 实际执行占用资源的操作，如何判断一个线程获取到锁了，由具体继承者去实现
    // release, releaseShared: 定义了释放资源的逻辑，释放之后，通知后续节点进行争抢锁
    // tryRelease, tryReleaseShared: 实际执行资源释放操作，具体由AQS的继承者去实现

    // 1.如何判断一个锁的状态或者说拥有者
    public volatile AtomicReference<Thread> owner = new AtomicReference<>();

    // 保存正在等待的线程
    private volatile LinkedBlockingDeque<Thread> waiters = new LinkedBlockingDeque<>();

    // 资源状态
    public volatile AtomicInteger state = new AtomicInteger(0);

    // 独占锁

    // 获取资源
    public void acquire() {
        boolean addQ = true;
        while (!tryAcquire()) {
            if (addQ) {
                // 没拿到锁，加入到等待集合
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                // 挂起当前线程
                LockSupport.park();
            }
        }
        // 将线程从等待集合移除
        waiters.remove(Thread.currentThread());
    }

    // 交给使用者实现，模板方法设计模式
    public boolean tryAcquire() {
        throw new UnsupportedOperationException();
    }

    // 定义了释放资源的操作
    public void release() {
        if (tryRelease()) {
            // 释放成功，通知等待者
            waiters.forEach(LockSupport::unpark);
        }
    }

    // 交给使用者实现
    public boolean tryRelease() {
        throw new UnsupportedOperationException();
    }

    // 共享锁

    // 获取锁
    public void acquireShared() {
        boolean addQ = true;
        while (tryAcquireShared() < 0) {
            if (addQ) {
                // 没拿到锁，加入到等待集合
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                // 挂起当前线程
                LockSupport.park();
            }
        }
        // 将线程从等待集合移除
        waiters.remove(Thread.currentThread());
    }

    // 交给使用者去实现
    // 返回资源的占用情况
    public int tryAcquireShared() {
        throw new UnsupportedOperationException();
    }

    // 释放锁
    public void releaseShared() {
        if (tryReleaseShared()) {
            // 释放成功，通知等待者
            waiters.forEach(LockSupport::unpark);
        }
    }

    // 交给使用者去实现
    public boolean tryReleaseShared() {
        throw new UnsupportedOperationException();
    }

    public AtomicInteger getState() {
        return state;
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }
}
