package com.mason.stage_one.chapter_one.section_one.thread_pool.source;

/**
 * Created by mwu on 2020/4/3
 */
public interface RunnableFuture<V> extends Runnable, Future<V> {
    /**
     * Sets this Future to the result of its computation
     * unless it has been cancelled.
     */
    void run();
}
