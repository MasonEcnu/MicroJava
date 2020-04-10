package com.mason.stage_one.chapter_one.section_two.atom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by WM on 2020/4/2
 */
public class AtomDemo {

    private static final Unsafe unsafe;
    private static final long valueOffset;

    private int value;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            valueOffset = unsafe.objectFieldOffset
                    (AtomDemo.class.getDeclaredField("value"));

        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public AtomDemo(int value) {
        this.value = value;
    }

    public void add() {
        // CAS+循环
        int current;
        do {
            current = unsafe.getIntVolatile(this, valueOffset);
        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));
    }

    public static void main(String[] args) throws InterruptedException {
        AtomDemo demo = new AtomDemo(0);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    demo.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(demo.value);
    }
}
