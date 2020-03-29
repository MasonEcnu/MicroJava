package com.mason.stage_one.chapter_one.section_three.source;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Created by WM on 2020/3/21
 * 可重入互斥锁
 * 可重入：同一个线程可以对同一个共享资源重复枷锁和释放
 * 独占锁
 * <p>
 * 与synchronized锁具有同样的功能语义
 * 同时实现公平和非公平锁
 */
public class ReentrantLock implements Lock, java.io.Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    private final Sync sync;

    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        abstract void lock();

        /**
         * 尝试获取非公平锁
         *
         * @param acquires 锁参数
         * @return 是否成功获取锁
         */
        final boolean nonfairTryAcquire(int acquires) {
            // 当前线程
            final Thread current = Thread.currentThread();
            // 同步器状态
            int c = getState();
            // state==0：表示当前锁没有被持有
            if (c == 0) {
                // CAS设置state，获取锁
                if (compareAndSetState(0, acquires)) {
                    // 成功则set锁持有线程
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                // todo 可重入体现
                // 如果当前线程就是锁的持有者
                int nextc = c + acquires;
                // 判断其持有数量是否overflow
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                // 设置state
                setState(nextc);
                return true;
            }
            // 获取锁失败，线程需要进入同步队列
            return false;
        }

        /**
         * protected修饰的，是实现了AQS定义的抽象方法
         * 释放锁
         * 公平和非公平通用
         *
         * @param releases 锁状态参数
         * @return 是否成功释放锁
         */
        protected final boolean tryRelease(int releases) {
            // 同步器状态减去releases，releases通常为1
            int c = getState() - releases;
            // 如果当前线程并没有持有锁，抛异常
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            // 当state=0时，表示锁空闲
            if (c == 0) {
                free = true;
                // 设置锁持有者为null
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        /**
         * @return 是否独占模式
         */
        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        /**
         * @return 条件队列
         */
        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        /**
         * 获取锁的持有者，独占模式
         *
         * @return state==0：表示没有现成持有锁
         */
        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        final void lock() {
            // 直接CAS尝试获取锁
            if (compareAndSetState(0, 1))
                // 如果成功，则记录
                setExclusiveOwnerThread(Thread.currentThread());
            else
                // 如果失败，则走正常流程获取锁
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    // 公平锁
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        // 尝试获取锁，失败则进入同步队列阻塞
        final void lock() {
            // AQS中的方法
            acquire(1);
        }

        // 由子类实现的尝试获取方法
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // hasQueuedPredecessors：公平性实现
                // 会判断当前线程是不是属于同步队列的头节点的下一个节点(头节点是释放锁的节点)
                // 如果是(返回false)，符合先进先出的原则，可以获得锁
                // 如果不是(返回true)，则继续等待
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                // todo 可重入体现
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

    /**
     * 无参构造器
     * 默认非公平锁
     */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * 有参构造器
     *
     * @param fair true->公平锁，false->非公平锁
     */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    public void lock() {
        sync.lock();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    // 释放锁
    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public int getHoldCount() {
        return sync.getHoldCount();
    }

    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }

    public boolean isLocked() {
        return sync.isLocked();
    }

    public final boolean isFair() {
        return sync instanceof FairSync;
    }

    protected Thread getOwner() {
        return sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }

    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }

    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }
}

