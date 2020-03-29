package com.mason.stage_one.chapter_one.section_one.run_time;

/**
 * Created by WM on 2020/3/28
 * Java运行过程示例
 * 编译class文件结果
 * <p>
 * javac RunTimeDemo.java
 * <p>
 * javap -v RunTimeDemo.class > RunTimeDemo.txt
 */
public class RunTimeDemo {
    public static void main(String[] args) {
        int x = 500;
        int y = 100;
        int a = x / y;
        int b = 50;
        System.out.println(a + b);
    }
}
