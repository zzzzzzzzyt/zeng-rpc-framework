package service.netty_call;

import service.netty_bootstrap.NettyClientBootStrap;

//客户端启动类
public class NettyClientCall {
    public static void main(String[] args) throws InterruptedException {
        NettyClientBootStrap.start("127.0.0.1",6668);
    }
}
