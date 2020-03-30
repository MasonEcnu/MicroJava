package com.mason.stage_one.chapter_one.section_one.thread_pool;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by WM on 2020/3/29
 * 线程池
 */
public class ThreadPoolDemo {

    /**
     * 测试
     * 提交15个执行时间需要3秒的任务
     * 看线程池的执行状况
     *
     * @param threadPoolExecutor 线程池
     * @throws Exception 异常
     */
    private void testCommon(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        // 测试：提交15个执行时间需要3秒的任务
        // 看超过大小的2个，对应的处理情况
        for (int i = 0; i < 15; i++) {
            int n = i;
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println("=====执行开始：" + n + "=====");
                    Thread.sleep(3000L);
                    System.out.println("*****执行结束：" + n + "*****");
                } catch (InterruptedException e) {
                    System.out.println("异常：" + e.getMessage());
                }
            });
            System.out.println("#####任务提交成功：" + i + "#####");
        }
        // 查看线程数量，查看队列等待数量
        Thread.sleep(500L);
        System.out.println("当前线程池的线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待任务的数量：" + threadPoolExecutor.getQueue().size());
        // 等待15秒，查看线程数量和等待数量
        // 理论上，超出核心线程数量的线程会自动销毁
        Thread.sleep(15000L);
        System.out.println("15秒后");
        System.out.println("当前线程池的线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待任务的数量：" + threadPoolExecutor.getQueue().size());
    }

    /**
     * 1.线程池信息
     * 核心线程数：5
     * 最大数量：10
     * 无界队列
     * 超出核心线程数量的线程存活时间：5秒
     * 默认拒绝策略：AbortPolicy
     * 异常：RejectedExecutionException
     *
     * @throws Exception 异常
     */
    public void threadPoolExecutorTest1() throws Exception {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        testCommon(poolExecutor);
        // 预计结果
        // 线程池线程数量：5
        // 超出数量的任务进入等待队列
    }

    /**
     * 2.线程池信息
     * 核心线程数：5
     * 最大数量：10
     * 等待队列大小：3
     * 超出核心线程数量的线程存活时间：5秒
     * 指定拒绝策略
     *
     * @throws Exception 异常
     */
    public void threadPoolExecutorTest2() throws Exception {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3), (runnable, executor) -> System.out.println("有任务被拒绝执行了。。。"));
        testCommon(poolExecutor);
        // 预计结果
        // 1.5个任务直接分配线程开始执行
        // 2.3个任务进入等待队列
        // 3.队列不够用，临时加开5个线程来执行任务（5秒没活干就销毁）
        // 4.队列和线程池都满了，剩下的两个任务，拒绝执行
        // 5.任务执行，5秒后，如果无任务可执行，销毁临时创建的5个线程
    }

    /**
     * 3.线程池信息
     * 核心线程数：5
     * 最大数量：5
     * 无界队列
     * 默认拒绝策略
     *
     * @throws Exception 异常
     */
    public void threadPoolExecutorTest3() throws Exception {
//        Executors.newFixedThreadPool(5);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        testCommon(poolExecutor);
        // 预计结果
        // 线程池线程数量：5
        // 超出数量的任务进入等待队列
    }

    /**
     * 4.线程池信息
     * 核心线程数：0
     * 最大数量：Integer.MAX_VALUE
     * 队列：SynchronousQueue
     * 默认拒绝策略
     * <p>
     * 适用于无法预估线程数量时
     *
     * @throws Exception 异常
     */
    public void threadPoolExecutorTest4() throws Exception {
        // SynchronousQueue：实际上它不是一个真正的队列
        // 因为它不会为队列中的元素维护存储空间
        // 与其他队列不同的是，它维护一组线程，这些线程在等待着把元素加入或移出队列

        // 在使用SynchronousQueue作为工作队列的前提下
        // 客户端代码向线程池提交任务时
        // 而线程池中又没有空闲的线程能够从SynchronousQueue队列中取出一个任务
        // 那么相应的offer调用就会失败，即任务没有被存入工作队列
        // 此时，ThreadPoolExecutor会创建一个新的工作线程
        // 用于对这个入队失败的任务进行处理
        // 假设此时线程池的带下还未达到最大线程池大小maximumPoolSize

        // Executors.newCachedThreadPool();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
        testCommon(poolExecutor);
        // 预计结果
        // 1.线程池线程数量：15，超出数量的任务，进入同步等待队列
        // 2.所有任务执行结束后，60秒后，如果无任务可执行
        // 销毁所有线程，线程池大小恢复到0
        Thread.sleep(60000L);
        System.out.println("60秒后，线程池中的线程数量：" + poolExecutor.getPoolSize());
    }

    /**
     * 5.线程池信息
     * 周期定时任务线程池
     * 核心线程数：5
     * 最大数量：Integer.MAX_VALUE
     * 默认队列：DelayedWorkQueue
     * 默认拒绝策略
     */
    public void threadPoolExecutorTest5() {
        //        Executors.newScheduledThreadPool(5);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        executor.schedule(() -> {
            // 延迟3秒后执行，执行一次
            System.out.println("定时任务被执行，现在时间是：" + System.currentTimeMillis() / 1000);
        }, 3000L, TimeUnit.MILLISECONDS);
        System.out.println("定时任务被提交，现在时间是：" + System.currentTimeMillis() / 1000);
        System.out.println("当前线程池中的线程数量：" + executor.getPoolSize());
    }

    /**
     * 6.线程池信息
     * 定时任务线程池
     * 核心线程数：0
     * 最大数量：Integer.MAX_VALUE
     * 队列：SynchronousQueue
     * 默认拒绝策略
     * <p>
     * 适用于无法预估线程数量时
     */
    public void threadPoolExecutorTest6() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        // 周期性执行某一任务，线程池提供了两种调度方式
        // 测试场景，提交的任务需要3秒才能执行完，看两种调度方式的区别
        // 1.提交任务，2秒后开始第一次执行，之后每间隔1秒，执行一次
        // 如果发现上次执行还未结束，则等待执行结束，结束后立即执行一次
        // 也就是说，这个代码是3秒执行一次
        // 计算：每次执行3秒，间隔1秒，执行结束后，立即执行，无需等待

        executor.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务-1被执行了，现在时间是：" + System.currentTimeMillis() / 1000);
        }, 2000L, 1000L, TimeUnit.MILLISECONDS);

        // 2.提交任务，2秒后开始第一次执行，之后每隔1秒，执行一次
        // 如果发现上次执行还未结束，则等待结束，等上次执行完毕后，开始计时，等待1秒
        // 也就是说，这个代码是4秒执行一次
        // 计算：每次执行3秒，间隔1秒，执行完毕后等待1秒再执行

        executor.scheduleWithFixedDelay(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务-2被执行了，现在时间是：" + System.currentTimeMillis() / 1000);
        }, 2000L, 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * 7.终止线程池
     * 线程池信息
     * 核心线程数：5
     * 最大数量：10
     * 队列大小：3
     * 超出核心线程数量的线程存活时间：5秒
     * 指定拒绝策略
     */
    public void threadPoolExecutorTest7() throws Exception {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3), (runnable, executor) -> System.out.println("有任务被拒绝执行了。。。"));
        for (int i = 0; i < 15; i++) {
            int n = i;
            poolExecutor.submit(() -> {
                try {
                    System.out.println("=====执行开始：" + n + "=====");
                    Thread.sleep(3000L);
                    System.out.println("*****执行结束：" + n + "*****");
                } catch (InterruptedException e) {
                    System.out.println("异常：" + e.getMessage());
                }
            });
            System.out.println("#####任务提交成功：" + i + "#####");
        }
        // 1秒后终止线程池
        Thread.sleep(1000L);
        poolExecutor.shutdown();
        // 再次提交一个任务
        poolExecutor.submit(() -> System.out.println("追加一个任务。。。"));

        // 结果分析
        // 1.10个任务被执行，3个任务进入等待队列，2个任务被拒绝
        // 2.调用shutdown后，不再接收新任务，等待13个任务执行结束
        // 3.追加的任务在线程池关闭后，无法提交，会被拒绝
    }

    /**
     * 8.立即终止线程池
     * 线程池信息
     * 核心线程数：5
     * 最大数量：10
     * 队列大小：3
     * 超出核心线程数量的线程存活时间：5秒
     * 指定拒绝策略
     */
    public void threadPoolExecutorTest8() throws Exception {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3), (runnable, executor) -> System.out.println("有任务被拒绝执行了。。。"));
        for (int i = 0; i < 15; i++) {
            int n = i;
            poolExecutor.submit(() -> {
                try {
                    System.out.println("=====执行开始：" + n + "=====");
                    Thread.sleep(3000L);
                    System.out.println("*****执行结束：" + n + "*****");
                } catch (InterruptedException e) {
                    System.out.println("异常：" + e.getMessage());
                }
            });
            System.out.println("#####任务提交成功：" + i + "#####");
        }
        // 1秒后终止线程池
        Thread.sleep(1000L);
        List<Runnable> shutdownNow = poolExecutor.shutdownNow();
        // 再次提交一个任务
        poolExecutor.submit(() -> System.out.println("追加一个任务。。。"));

        System.out.println("未结束的任务数量：" + shutdownNow.size());
        // 结果分析
        // 1.10个任务被执行，3个任务进入等待队列，2个任务被拒绝
        // 2.调用shutdownNow后，不再接收新任务
        // 正在执行的任务，直接interrupted，等待队列中的任务返回
        // 3.追加的任务在线程池关闭后，无法提交，会被拒绝
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolDemo demo = new ThreadPoolDemo();
//        demo.threadPoolExecutorTest1();
//        demo.threadPoolExecutorTest2();
//        demo.threadPoolExecutorTest3();
//        demo.threadPoolExecutorTest4();
//        demo.threadPoolExecutorTest5();
//        demo.threadPoolExecutorTest6();
//        demo.threadPoolExecutorTest7();
        demo.threadPoolExecutorTest8();
    }
}
