package consumer.bootstrap.netty;

import consumer.netty.NettyClient20;

/*
    以netty为网络编程框架的消费者端启动类
 */
//进行启动 提供类的方式即可
public class NettyConsumerBootStrap20 {
    public static void main(String[] args) throws InterruptedException {
        NettyClient20.start(args[0], Integer.parseInt(args[1]));
    }
}
