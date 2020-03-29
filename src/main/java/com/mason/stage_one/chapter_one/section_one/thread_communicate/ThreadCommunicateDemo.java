package com.mason.stage_one.chapter_one.section_one.thread_communicate;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by WM on 2020/3/29
 * 线程通信
 */
public class ThreadCommunicateDemo {

    private static Object baozidian = null;

    /**
     * 正常的suspend/resume
     *
     * @throws Exception 中断异常
     */
    public void suspendResumeTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.进入等待");
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }

    /**
     * suspend不会释放锁
     *
     * @throws Exception 中断异常
     */
    public void suspendResumeDeadLockTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.进入等待");
                synchronized (this) {
                    Thread.currentThread().suspend();
                }
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this) {
            consumerThread.resume();
        }
        System.out.println("3.通知消费者");
    }

    /**
     * resume在suspend之前执行
     *
     * @throws Exception 中断异常
     */
    public void suspendResumeDeadLockTwoTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.没包子了，进入等待");
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 这里使suspend在resume之后执行
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3.通知消费者");
        consumerThread.join();
    }

    /**
     * 正常
     *
     * @throws Exception 中断异常
     */
    public void waitNotifyTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                synchronized (this) {
                    try {
                        System.out.println("1.没包子了，进入等待");
                        // 释放锁
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this) {
            this.notifyAll();
        }
        System.out.println("3.通知消费者");
    }

    /**
     * wait/notify死锁
     *
     * @throws Exception 中断异常
     */
    public void waitNotifyDeadLockTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                try {
                    Thread.sleep(5000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    try {
                        System.out.println("1.没包子了，进入等待");
                        // 释放锁
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this) {
            this.notifyAll();
        }
        System.out.println("3.通知消费者");
    }

    /**
     * 没有顺序限制
     * 不需要在同步代码块内执行
     *
     * @throws Exception 中断异常
     */
    public void parkUnparkTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.没包子了，进入等待");
                try {
                    Thread.sleep(5000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LockSupport.park();
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        LockSupport.unpark(consumerThread);
        System.out.println("3.通知消费者");
    }

    /**
     * 由于park并不会释放锁
     * 所以还是有可能死锁的
     *
     * @throws Exception 中断异常
     */
    public void parkUnparkDeadLockTest() throws Exception {
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.没包子了，进入等待");
                synchronized (this) {
                    LockSupport.park();
                }
            }
            System.out.println("2.买到包子了，回家");
        }, "consumerThread");

        consumerThread.start();
        // 3秒之后，生成一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this) {
            LockSupport.unpark(consumerThread);
        }
        System.out.println("3.通知消费者");
    }

    public static void main(String[] args) throws Exception {
        ThreadCommunicateDemo demo = new ThreadCommunicateDemo();
//        demo.suspendResumeTest();
//        demo.suspendResumeDeadLockTest();
//        demo.suspendResumeDeadLockTwoTest();
//        demo.waitNotifyTest();
//        demo.waitNotifyDeadLockTest();
//        demo.parkUnparkTest();
//        demo.parkUnparkDeadLockTest();
    }
}
