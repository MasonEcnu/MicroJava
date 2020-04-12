package com.mason.stage_one.chapter_one.section_three.future;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by WM on 2020/4/12
 * 模仿FutureTask
 */
public class MasonFutureTask<T> implements Runnable, Future {

    /**
     * 业务逻辑在Callable里
     */
    private final Callable<T> callable;

    private T result = null;

    private volatile FutureTaskState state;

    // 存储等待者的集合
    private LinkedBlockingDeque<Thread> waiters = new LinkedBlockingDeque<>();

    public MasonFutureTask(Callable<T> callable) {
        this.callable = callable;
        this.state = FutureTaskState.NEW;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            state = FutureTaskState.END;
        }
        // 唤醒等待者
        waiters.forEach(LockSupport::unpark);
    }

    /**
     * 返回结果
     *
     * @return T 结果
     */
    public T get() {
        // 如果没有结束，那么调用get方法的线程就进入等待
        if (state == FutureTaskState.END) {
            waiters.remove(Thread.currentThread());
            return result;
        }
        // 存起来，进入等待
        waiters.offer(Thread.currentThread());
        while (state != FutureTaskState.END) {
            LockSupport.park();
        }
        return result;
    }

    @Override
    public Object get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    enum FutureTaskState {
        NEW,
        END,
    }
}
