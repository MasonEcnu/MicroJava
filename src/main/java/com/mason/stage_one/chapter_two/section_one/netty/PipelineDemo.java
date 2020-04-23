package com.mason.stage_one.chapter_two.section_one.netty;

/**
 * Created by WM on 2020/4/19
 * 链表实现的责任链模式demo
 * Netty中使用的方式
 */
public class PipelineDemo {

    /**
     * 初始化的时候构造一个head
     * 作为责任链的开始
     * 但是没有具体的处理
     */
    public HandlerChainContext head = new HandlerChainContext(new AbstractHandler() {
        @Override
        void doHandle(HandlerChainContext chainContext, Object arg0) {
            chainContext.runNext(arg0);
        }
    });

    public void requestProcess(Object arg0) {
        this.head.handle(arg0);
    }

    public void addLast(AbstractHandler handler) {
        HandlerChainContext context = head;
        while (context.getNext() != null) {
            context = context.getNext();
        }
        context.setNext(new HandlerChainContext(handler));
    }

    public static void main(String[] args) {
        PipelineDemo pipeline = new PipelineDemo();
        pipeline.addLast(new FirstHandler());
        pipeline.addLast(new SecondHandler());

        // 发起请求
        pipeline.requestProcess("~~~嘿哈<=>呼哈~~~");
    }
}

/**
 * handler上下文
 * 主要负责维护链和链的执行
 */
class HandlerChainContext {
    // 下一个节点
    private HandlerChainContext next;
    // 处理器
    private final AbstractHandler handler;

    public HandlerChainContext(AbstractHandler handler) {
        this.handler = handler;
    }

    void handle(Object arg0) {
        this.handler.doHandle(this, arg0);
    }

    void runNext(Object arg0) {
        if (this.next != null) {
            this.next.handle(arg0);
        }
    }

    public HandlerChainContext getNext() {
        return next;
    }

    public void setNext(HandlerChainContext next) {
        this.next = next;
    }
}

/**
 * 处理抽象类
 */
abstract class AbstractHandler {

    /**
     * 处理器
     * 这个处理器就做一件事
     * 在传入的字符串中增加一个尾巴
     *
     * @param chainContext 上线文
     * @param arg0         传递参数
     */
    abstract void doHandle(HandlerChainContext chainContext, Object arg0);
}

/**
 * 处理器的具体实现类
 */
class FirstHandler extends AbstractHandler {

    @Override
    void doHandle(HandlerChainContext chainContext, Object arg0) {
        arg0 = arg0.toString() + "...first handler的小尾巴...";
        System.out.println("我是FirstHandler的实例，我在处理：" + arg0);
        // 执行下一个
        chainContext.runNext(arg0);
    }
}

class SecondHandler extends AbstractHandler {

    @Override
    void doHandle(HandlerChainContext chainContext, Object arg0) {
        arg0 = arg0.toString() + "...second handler的小尾巴...";
        System.out.println("我是SecondHandler的实例，我在处理：" + arg0);
        // 执行下一个
        chainContext.runNext(arg0);
    }
}