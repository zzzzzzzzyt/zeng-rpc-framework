package consumer.bootstrap;

import consumer.nio.NIOClient;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类
 */
public class NIOConsumerBootStrap {
    public static void main(String[] args) throws IOException {
        NIOClient.start("127.0.0.1",6666);
    }
}
