package com.mason.stage_one.chapter_one.section_two.atom;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

/**
 * Created by WM on 2020/4/2
 */
public class LongAccumulatorDemo {

    public static void main(String[] args) throws InterruptedException {
        // 操作方式
        // 左右代表放入数据的顺序
        // left=第一个放入的数据
        // right=第二个放入的数据
        // 以此类推
        LongBinaryOperator operator = (left, right) -> left > right ? left : right;
        LongAccumulator accumulator = new LongAccumulator(operator, 0);

        // 1000个线程
        for (int i = 0; i <= 1000; i++) {
            int finalI = i;
            new Thread(() -> accumulator.accumulate(finalI)).start();
        }
        Thread.sleep(2000L);
        System.out.println(accumulator.get());
    }
}
