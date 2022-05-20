package provider.bootstrap.nio;


import provider.nio.NIOBlockingServer11;

/*
    以nio为网络编程框架的服务提供端启动类
 */

/**
 * @author 祝英台炸油条
 */
public class NIOProviderBootStrap11 {
    public static void main(String[] args) {
        //阻塞启动
        NIOBlockingServer11.start(6666);
    }
}
