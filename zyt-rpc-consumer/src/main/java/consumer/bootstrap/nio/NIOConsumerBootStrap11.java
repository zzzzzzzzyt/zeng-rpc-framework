package consumer.bootstrap.nio;

import consumer.nio.NIOBlockingClient11;

/*
    以nio为网络编程框架的消费者端启动类
 */

/**
 * @author 祝英台炸油条
 */
public class NIOConsumerBootStrap11 {
    public static void main(String[] args) {
        //阻塞启动
        NIOBlockingClient11.start("127.0.0.1", 6666);
    }
}
