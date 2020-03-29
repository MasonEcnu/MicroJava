package com.mason.stage_one.chapter_one.section_one.thread_stop;

/**
 * Created by WM on 2020/3/28
 * 线程中止
 */
public class StopThreadDemo {

    /**
     * 执行结果
     * i=1,j=0
     * 神奇
     */
    public static void main(String[] args) throws InterruptedException {
        StopThread thread = new StopThread();
        thread.start();
        Thread.sleep(1000L);
        // 错误方式
        // thread.stop();
        thread.interrupt();
        while (thread.isAlive()) {
            // 确保线程执行完毕
        }
        thread.print();
    }
}
