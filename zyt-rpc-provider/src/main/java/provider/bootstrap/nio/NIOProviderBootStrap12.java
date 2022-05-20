package provider.bootstrap.nio;


import provider.nio.NIONonBlockingServer12bye;
import provider.nio.NIONonBlockingServer12hello;

/*
    以nio为网络编程框架的服务提供端启动类  加入了zk
 */

/**
 * @author 祝英台炸油条
 */
public class NIOProviderBootStrap12 {
    public static void main(String[] args) {

        //启动
        new Thread(() -> {
            //因为每个服务提供端内部都是在监听循环阻塞 每个开启一个线程进行监听
            NIONonBlockingServer12hello.start(6666);
        }).start();
        new Thread(() -> {
            //因为每个服务提供端内部都是在监听循环阻塞 每个开启一个线程进行监听
            NIONonBlockingServer12hello.start(6668);
        }).start();

        //启动
        new Thread(() -> NIONonBlockingServer12bye.start(6667)).start();
    }
}
