package provider.bootstrap;

import provider.nio.NIONonBlockingServer10;

import java.io.IOException;

/*
    以nio为网络编程框架的服务提供端启动类
 */
public class NIOProviderBootStrap10 {
    public static void main(String[] args) throws IOException {
        //非阻塞启动
        NIONonBlockingServer10.start(6666);
    }
}
