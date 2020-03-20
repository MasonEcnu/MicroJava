package com.mason.stage_one.chapter01.practice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mwu on 2020/3/20
 */
public class AtomDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.getAndIncrement();
    }
}
