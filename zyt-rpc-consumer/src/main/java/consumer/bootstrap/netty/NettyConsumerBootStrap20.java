package consumer.bootstrap.netty;

import consumer.netty.NettyClient20;

/*
    以netty为网络编程框架的消费者端启动类
 */
//进行启动 提供类的方式即可
/**
 * @author 祝英台炸油条
 */
public class NettyConsumerBootStrap20 {
    public static void main(String[] args) {
        //在这里 第一参数是ip地址 第二个参数是端口号
        NettyClient20.start(args[0], Integer.parseInt(args[1]));
    }
}
