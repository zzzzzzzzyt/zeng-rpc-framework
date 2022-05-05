package service.call.netty_call;

import org.apache.zookeeper.KeeperException;
import service.netty_bootstrap.NettyServerBootStrap;

import java.io.IOException;

//启动类 给定对应的端口 进行启动并监听
public class NettyServerCall {
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        NettyServerBootStrap.start();
    }
}
