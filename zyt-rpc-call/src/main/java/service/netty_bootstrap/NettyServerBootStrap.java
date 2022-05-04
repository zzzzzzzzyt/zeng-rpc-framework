package service.netty_bootstrap;

import provider.bootstrap.netty.NettyProviderBootStrap20;

public class NettyServerBootStrap {
    public static void start(String address,int port) throws InterruptedException {
        NettyProviderBootStrap20.main(new String[]{address, String.valueOf(port)});
    }
}
