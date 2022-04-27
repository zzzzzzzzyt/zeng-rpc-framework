package consumer.bootstrap;

import consumer.nio.NIOBlockingClient;
import consumer.nio.NIONonBlockingClient;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类
 */
public class NIOConsumerBootStrap {
    public static void main(String[] args) throws IOException {

        //非阻塞启动
        // NIONonBlockingClient.start("127.0.0.1",6666);

        //阻塞启动
        NIOBlockingClient.start("127.0.0.1",6666);
    }
}
