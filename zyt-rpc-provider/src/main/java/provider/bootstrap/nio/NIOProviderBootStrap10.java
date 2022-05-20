package provider.bootstrap.nio;

import provider.nio.NIONonBlockingServer10;

/*
    以nio为网络编程框架的服务提供端启动类
 */

/**
 * @author 祝英台炸油条
 */
public class NIOProviderBootStrap10 {
    public static void main(String[] args) {
        //非阻塞启动
        NIONonBlockingServer10.start(6666);
    }
}
