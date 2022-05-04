package service.netty_call;

import service.netty_bootstrap.NettyServerBootStrap;

//启动类 给定对应的端口 进行启动并监听
public class NettyServerCall {
    public static void main(String[] args) throws InterruptedException {
        NettyServerBootStrap.start("127.0.0.1",6668);
    }
}
