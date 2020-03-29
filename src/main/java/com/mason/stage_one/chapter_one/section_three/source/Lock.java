package com.mason.stage_one.chapter_one.section_three.source;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Created by WM on 2020/3/22
 */
public interface Lock {

    /**
     * 获取锁方法
     * 获取不到的线程会被加入到同步队列中
     * 阻塞排毒
     */
    void lock();

    /**
     * 获取可中断的锁
     *
     * @throws InterruptedException 中断异常
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 尝试获取锁
     * 如果锁空闲，获取成功返回true
     *
     * @return 是否成功获取到锁
     */
    boolean tryLock();

    /**
     * 带超时等待的尝试获取锁方法
     * 如果超时且没有获取到锁
     * 则返回false
     *
     * @param time 时长
     * @param unit 单位
     * @return 是否成功获取到锁
     * @throws InterruptedException 中断异常
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 条件队列
     *
     * @return Condition
     */
    Condition newCondition();
}

