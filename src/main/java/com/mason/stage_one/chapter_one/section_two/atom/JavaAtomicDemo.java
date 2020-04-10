package com.mason.stage_one.chapter_one.section_two.atom;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.function.DoubleBinaryOperator;

/**
 * Created by WM on 2020/4/2
 */
public class JavaAtomicDemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    atomicInteger.getAndIncrement();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(atomicInteger.get());

        DoubleBinaryOperator operator = (left, right) -> left + right;
        DoubleAccumulator accumulator = new DoubleAccumulator(operator, 0.0);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    accumulator.accumulate(1);
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(accumulator.get());
    }
}
