package com.mason.stage_one.chapter_one.section_one.thread_pool.source;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by WM on 2020/3/29
 */
public interface ExecutorService extends Executor {

    /**
     * 关闭线程池
     * 不接受新任务
     * 执行已存在的任务，但不等待它们执行结束
     * awaitTermination
     */
    void shutdown();

    /**
     * 尝试停止所有正在执行的任务
     * 停止等待的任务处理
     * 并返回等待执行的任务列表
     *
     * @return List<Runnable>  等待执行的任务列表
     */
    List<Runnable> shutdownNow();

    // 当前线程池是否已经关闭
    boolean isShutdown();

    // 如果所有线程执行完毕后，线程池Shutdown，则返回true
    boolean isTerminated();

    /**
     * 检测ExecutorService是否已经关闭
     * 直到所有任务执行完毕
     * 或超时、或当前线程中断
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return boolean 是否执行完毕
     * @throws InterruptedException 中断异常
     */
    boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException;

    /**
     * 提交一个用于执行的Callable返回任务
     * 并返回一个Future
     * 用于获取Callable执行的结果
     *
     * @param task 任务
     * @param <T>  结果泛型
     * @return Future
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 提交可运行的任务
     * 并返回一个Future对象
     * 执行结果为传入的result
     *
     * @param task   任务
     * @param result 结果
     * @param <T>    结果泛型
     * @return Future
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
     * 提交可运行的任务Runnable
     * 执行成功，则future返回结果为null
     *
     * @param task 任务
     * @return Future
     */
    Future<?> submit(Runnable task);

    /**
     * 执行给定的任务集合
     * 执行完毕后返回结果
     *
     * @param tasks 任务集合
     * @param <T>   结果泛型
     * @return 执行结果
     * @throws InterruptedException 中断异常
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException;

    /**
     * 执行给定的任务集合
     * 执行完毕或超时，返回结果
     * 其他任务终止
     *
     * @param tasks   任务集合
     * @param timeout 超时时长
     * @param unit    时间单位
     * @param <T>     结果泛型
     * @return 执行结果
     * @throws InterruptedException 中断异常
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
            throws InterruptedException;

    /**
     * 给定任务集合中
     * 任意一个执行完毕
     * 则返回
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 执行完成的任务
     * @throws InterruptedException 中断异常
     * @throws ExecutionException   执行异常
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException;

    /**
     * 给定任务集合中
     * 任意一个执行完毕或超时
     * 则返回
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 执行完成的任务
     * @throws InterruptedException 中断异常
     * @throws ExecutionException   执行异常
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;
}

