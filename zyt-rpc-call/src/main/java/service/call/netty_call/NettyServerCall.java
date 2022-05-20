package service.call.netty_call;

import service.netty_bootstrap.NettyServerBootStrap;


//启动类 给定对应的端口 进行启动并监听
/**
 * @author 祝英台炸油条
 */
public class NettyServerCall {
    public static void main(String[] args) {
        NettyServerBootStrap.start();
    }
}
