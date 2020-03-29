package com.mason.stage_one.chapter_one.section_one.thread_stop;

/**
 * Created by WM on 2020/3/28
 */
public class StopThread extends Thread {

    private int i = 0, j = 0;

    @Override
    public void run() {
        synchronized (this) {
            ++i;
            try {
                // 模拟耗时工作
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
    }

    public void print() {
        System.out.format("i=%s, j=%s\n", i, j);
    }
}
