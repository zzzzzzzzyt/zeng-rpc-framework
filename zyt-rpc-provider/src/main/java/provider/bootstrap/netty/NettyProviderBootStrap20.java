package provider.bootstrap.netty;

import provider.netty.NettyServer20;

/*
    以netty为网络编程框架的服务提供端启动类
 */
/**
 * @author 祝英台炸油条
 */
public class NettyProviderBootStrap20 {
    public static void main(String[] args) {
        //传入要绑定的ip和端口
        NettyServer20.start(args[0], Integer.parseInt(args[1]));
    }
}
