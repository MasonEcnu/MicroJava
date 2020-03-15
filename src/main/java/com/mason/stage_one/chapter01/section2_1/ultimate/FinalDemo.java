package com.mason.stage_one.chapter01.section2_1.ultimate;

/**
 * Created by WM on 2020/3/15
 */
public class FinalDemo {

    public static void main(String[] args) throws InterruptedException {
        FinalDemo outer = new FinalDemo();
        FinalFieldDemo fieldDemo = outer.new FinalFieldDemo();
        for (int i = 0; i < 1000; i++) {
            new Thread(fieldDemo::writer).start();
            new Thread(fieldDemo::reader).start();
        }

        Thread.sleep(2000L);
    }

    class FinalFieldDemo {
        final int x;
        int y;

        // 如果f不为空，则f.x读取一定是最新的，final的语义决定
        FinalFieldDemo f;

        public FinalFieldDemo() {
            x = 3;
            y = 4;
        }

        void writer() {
            f = new FinalFieldDemo();
        }

        void reader() {
            if (f != null) {
                int i = f.x;    // 确保可以读取到x=3
                int j = f.y;    // 有可能会读取到0
                System.out.println(i + "," + j);
            }
        }
    }
}
