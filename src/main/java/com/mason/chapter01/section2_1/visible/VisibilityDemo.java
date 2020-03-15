package com.mason.chapter01.section2_1.visible;

import java.util.concurrent.TimeUnit;

/**
 * Created by WM on 2020/3/14
 */
public class VisibilityDemo {
    // client模式，不会进行指令重排序
    // 通过设置jvm参数
    // 打印出jit编译的内容（非class文件），可视化工具jitwatch进行查看
    // -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
    // -XX:+LogCompilation -XX:+LogFile=jit.log
    private volatile boolean flag = true;

    // 默认运行在server模式下
    // 因此会出现死循环
    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo demo = new VisibilityDemo();

        new Thread(() -> {
            int i = 0;
            // class -> 运行时jit编译 -> 汇编指令 -> 重排序
            // 导致死循环
//            if (demo.flag) {    // jvm优化，导致死循环
//                // hot code 热点代码重排序
//                while (true) { // 指令重排序
//                    i++;
//                }
//            }
            // hot code 热点代码重排序
            while (demo.flag) { // 指令重排序
                i++;
            }
            System.out.println(i);
        }).start();

        TimeUnit.SECONDS.sleep(2);

        demo.flag = false;

        System.out.println("flag被置为false");
    }
}
