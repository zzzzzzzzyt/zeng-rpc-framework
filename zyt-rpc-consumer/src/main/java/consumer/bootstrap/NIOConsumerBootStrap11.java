package consumer.bootstrap;

import consumer.nio.NIOBlockingClient11;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类
 */
public class NIOConsumerBootStrap11 {
    public static void main(String[] args) throws IOException {

        //阻塞启动
        NIOBlockingClient11.start("127.0.0.1",6666);

    }
}
