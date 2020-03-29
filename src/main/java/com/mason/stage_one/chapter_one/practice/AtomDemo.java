package com.mason.stage_one.chapter_one.practice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mwu on 2020/3/20
 */
public class AtomDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println(atomicInteger.incrementAndGet());
    }
}
