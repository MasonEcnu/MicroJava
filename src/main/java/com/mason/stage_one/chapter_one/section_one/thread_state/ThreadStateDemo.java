package com.mason.stage_one.chapter_one.section_one.thread_state;

/**
 * Created by WM on 2020/3/28
 * 线程状态
 */
public class ThreadStateDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("########第一种状态切换：新建->运行->终止########");
        Thread t1 = new Thread(() -> {
            Thread current = Thread.currentThread();
            System.out.format("执行中，%s当前状态：%s\n", current.getName(), current.getState());
            System.out.format("%s执行了\n", current.getName());
        }, "t1");
        System.out.format("未调用start方法，%s当前状态：%s\n", t1.getName(), t1.getState());
        t1.start();
        Thread.sleep(2000L);    // 等待t1执行完毕
        System.out.format("等待2秒，%s当前状态：%s\n", t1.getName(), t1.getState());

        // 注意：线程终止之后，再调用start()方法，会抛异常IllegalThreadStateException
        // t1.start();

        System.out.println();
        System.out.println("########第一种状态切换：新建->运行->等待->运行->终止########");
        Thread t2 = new Thread(() -> {
            Thread current = Thread.currentThread();
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.format("执行中，%s当前状态：%s\n", current.getName(), current.getState());
            System.out.format("%s执行了\n", current.getName());
        }, "t2");
        System.out.format("未调用start方法，%s当前状态：%s\n", t2.getName(), t2.getState());
        t2.start();
        System.out.format("调用start方法，%s当前状态：%s\n", t2.getName(), t2.getState());
        Thread.sleep(200L);
        System.out.format("等待200毫秒，%s当前状态：%s\n", t2.getName(), t2.getState());
        Thread.sleep(3000L);
        System.out.format("等待3秒，%s当前状态：%s\n", t2.getName(), t2.getState());
        System.out.println();

        System.out.println("########第一种状态切换：新建->运行->阻塞->运行->终止########");
        Thread t3 = new Thread(() -> {
            synchronized (ThreadStateDemo.class) {
                // 无法获取锁，则阻塞
                Thread current = Thread.currentThread();
                System.out.format("执行中，%s当前状态：%s\n", current.getName(), current.getState());
                System.out.format("%s执行了\n", current.getName());
            }
        }, "t3");
        synchronized (ThreadStateDemo.class) {
            System.out.format("未调用start方法，%s当前状态：%s\n", t3.getName(), t3.getState());
            t3.start();
            System.out.format("调用start方法，%s当前状态：%s\n", t3.getName(), t3.getState());
            Thread.sleep(200L);
            System.out.format("等待200毫秒，%s当前状态：%s\n", t3.getName(), t3.getState());
        }
        Thread.sleep(3000L);
        System.out.format("等待3秒，%s当前状态：%s\n", t3.getName(), t3.getState());
    }
}
