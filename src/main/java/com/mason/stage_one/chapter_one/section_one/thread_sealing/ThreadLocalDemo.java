package com.mason.stage_one.chapter_one.section_one.thread_sealing;

/**
 * Created by WM on 2020/3/29
 * 线程封闭
 */
public class ThreadLocalDemo {

    private static ThreadLocal<String> value = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // ThreadLocal
        value.set("这是主线程设置的123");   // 主线程设置值
        String v = value.get();
        System.out.println("线程1执行前，主线程设置的值：" + v);

        new Thread(() -> {
            String v1 = value.get();
            System.out.println("线程1取到的值：" + v1);

            value.set("线程1设置的值456");    // 线程t1设置值

            v1 = value.get();
            System.out.println("线程1重新设置之后的值：" + v1);
            System.out.println("线程1执行完毕");
        }, "t1").start();

        Thread.sleep(2000L);

        v = value.get();
        System.out.println("线程1执行之后，主线程取到的值：" + v);
    }
}
