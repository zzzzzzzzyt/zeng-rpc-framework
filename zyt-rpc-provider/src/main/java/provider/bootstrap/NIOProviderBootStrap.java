package provider.bootstrap;


import provider.nio.NIOBlockingServer;
import provider.nio.NIONonBlockingServer;

import java.io.IOException;

/*
    以nio为网络编程框架的服务提供端启动类
 */
public class NIOProviderBootStrap {
    public static void main(String[] args) throws IOException {

        //非阻塞启动
        // NIONonBlockingServer.start(6666);

        //阻塞启动
        NIOBlockingServer.start(6666);
    }
}
