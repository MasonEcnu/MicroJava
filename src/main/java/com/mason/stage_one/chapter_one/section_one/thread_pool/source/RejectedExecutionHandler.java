package com.mason.stage_one.chapter_one.section_one.thread_pool.source;

/**
 * Created by mwu on 2020/4/2
 */
public interface RejectedExecutionHandler {

    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
