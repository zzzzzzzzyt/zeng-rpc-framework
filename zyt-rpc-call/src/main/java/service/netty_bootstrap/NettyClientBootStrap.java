package service.netty_bootstrap;


import consumer.bootstrap.netty.NettyConsumerBootStrap20;

public class NettyClientBootStrap {
    public static void start(String address, int port) throws InterruptedException {
        NettyConsumerBootStrap20.main(new String[]{address, String.valueOf(port)});
    }
}
